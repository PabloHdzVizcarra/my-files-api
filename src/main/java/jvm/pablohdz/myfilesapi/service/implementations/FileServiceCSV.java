package jvm.pablohdz.myfilesapi.service.implementations;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jvm.pablohdz.myfilesapi.dto.CSVFileDataDto;
import jvm.pablohdz.myfilesapi.dto.CSVFileDto;
import jvm.pablohdz.myfilesapi.dto.FileServiceDataResponse;
import jvm.pablohdz.myfilesapi.exception.CSVFileAlreadyRegisteredException;
import jvm.pablohdz.myfilesapi.exception.FileNotRegisterException;
import jvm.pablohdz.myfilesapi.mapper.FileMapper;
import jvm.pablohdz.myfilesapi.model.MyFile;
import jvm.pablohdz.myfilesapi.model.User;
import jvm.pablohdz.myfilesapi.repository.MyFileRepository;
import jvm.pablohdz.myfilesapi.service.AuthenticationService;
import jvm.pablohdz.myfilesapi.service.FileStorageService;
import jvm.pablohdz.myfilesapi.service.FileService;
import jvm.pablohdz.myfilesapi.webhook.EventHook;
import jvm.pablohdz.myfilesapi.webhook.WebHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceCSV implements FileService {
  Logger logger = LoggerFactory.getLogger(FileServiceCSV.class);
  private final FileStorageService fileStorageService;
  private final AuthenticationService authenticationService;
  private final MyFileRepository fileRepository;
  private final FileMapper fileMapper;
  private final WebHook webHook;

  @Autowired
  public FileServiceCSV(
      FileStorageService fileStorageService,
      AuthenticationService authenticationService,
      MyFileRepository myFileRepository,
      FileMapper fileMapper,
      WebHook webHook) {
    this.fileStorageService = fileStorageService;
    this.authenticationService = authenticationService;
    this.fileRepository = myFileRepository;
    this.fileMapper = fileMapper;
    this.webHook = webHook;
  }

  @Override
  @Transactional
  public CSVFileDto uploadFile(MultipartFile file) {
    byte[] bytes = parseMultipartFileToBytes(file);
    String fileName = getFileName(file);
    verifyIfFileHasAlreadyRegistered(fileName);

    User currentUser = authenticationService.getCurrentUser();
    String keyFile = fileStorageService.upload(bytes, fileName, currentUser.getUsername());
    MyFile CSVFile = createFile(fileName, currentUser, keyFile);
    MyFile fileSaved = fileRepository.save(CSVFile);

    sendAddedEvent(fileSaved);
    return fileMapper.myFileToCSVFileDto(fileSaved);
  }

  /**
   * Send added event by webhook
   *
   * @param fileSaved a file before saved
   */
  private void sendAddedEvent(MyFile fileSaved) {
    EventHook addEvent =
        webHook.createAddEvent(
            fileSaved.getId(),
            fileSaved.getName(),
            List.of(),
            "http://localhost:8080/api/files/" + fileSaved.getId());
    webHook.sendEvent(addEvent);
  }

  @Override
  public FileServiceDataResponse download(String id) {
    MyFile file = getFileFromRepository(id);
    String storageId = file.getStorageId();
    String fileName = file.getName();
    ByteArrayResource data = fileStorageService.getFile(storageId);

    sendDownloadEvent(id, fileName);

    return new FileServiceDataResponse(fileName, data);
  }

  private void sendDownloadEvent(String id, String fileName) {
    EventHook event = webHook.createDownloadEvent(id, fileName, List.of());
    webHook.sendEvent(event);
    logger.info("download event is sending, the file with the id: {} it was downloaded", id);
  }

  private MyFile getFileFromRepository(String id) {
    Optional<MyFile> optionalMyFile = fileRepository.findById(id);
    return optionalMyFile.orElseThrow(() -> new FileNotRegisterException(id));
  }

  @Override
  @Transactional
  public CSVFileDataDto update(String id, MultipartFile file) {
    byte[] bytesFromMultipartFile = getBytesFromMultipartFile(file);
    String contentType = file.getContentType();
    String originalFilename = file.getOriginalFilename();
    MyFile foundFile = getFileFromRepository(id);
    updateFileInServerFiles(bytesFromMultipartFile, originalFilename, foundFile);
    updateFileInRepository(originalFilename, foundFile);
    sendEventUpdateToWebHook(id, originalFilename);

    logger.info("new update event its created to file with id: {}", id);
    return fileMapper.toCSVFileDataDto(originalFilename, contentType, bytesFromMultipartFile);
  }

  private void sendEventUpdateToWebHook(String id, String originalFilename) {
    EventHook updateEvent = webHook.createUpdateEvent(id, originalFilename, List.of());

    webHook.sendEvent(updateEvent);
  }

  private void updateFileInRepository(String originalFilename, MyFile foundFile) {
    foundFile.setName(originalFilename);
    fileRepository.save(foundFile);
  }

  private void updateFileInServerFiles(
      byte[] bytesFromMultipartFile, String originalFilename, MyFile foundFile) {
    String storageId = foundFile.getStorageId();
    fileStorageService.update(storageId, bytesFromMultipartFile, originalFilename);
  }

  private byte[] getBytesFromMultipartFile(MultipartFile file) {
    try {
      return file.getBytes();
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  private void verifyIfFileHasAlreadyRegistered(String filename) {
    Optional<MyFile> optionalMyFile = fileRepository.findByName(filename);
    if (optionalMyFile.isPresent()) throw new CSVFileAlreadyRegisteredException(filename);
  }

  private MyFile createFile(String fileName, User currentUser, String keyFile) {
    MyFile myFile = new MyFile();
    myFile.setName(fileName);
    myFile.setStorageId(keyFile);
    myFile.setVersion(1.0);
    myFile.setUser(currentUser);
    return myFile;
  }

  /**
   * Get the filename from the file
   *
   * @param file file in format CSV
   * @return the filename
   */
  private String getFileName(MultipartFile file) {
    return file.getOriginalFilename();
  }

  /**
   * Parse file {@code MultipartFile} to bytes arrays
   *
   * @param file the file to parsing
   * @return array bytes with the data
   */
  private byte[] parseMultipartFileToBytes(MultipartFile file) {
    try {
      return file.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Collection<CSVFileDto> getAllFilesByUserId(String userId) {
    User user = authenticationService.getCurrentUser();
    Collection<MyFile> allFilesByUser = fileRepository.findAllByUser(user);

    return allFilesByUser.stream()
        .map(fileMapper::toCSVFileDto)
        .collect(Collectors.toUnmodifiableList());
  }

  @Override
  public void deleteFile(String id) {
    MyFile file = getFileByIdFromRepository(id);
    deleteFileFromStorageServiceFiles(file);
    deleteFileFromRepositoryByFile(id, file);

    String fileName = file.getName();
    createEventDeleteFile(id, fileName);
  }

  private void createEventDeleteFile(String id, String fileName) {
    EventHook event = webHook.createDeleteEvent(id, fileName, List.of());
    webHook.sendEvent(event);
    logger.debug("new delete event is send, the resource that be deleted contains the id: {}", id);
  }

  private void deleteFileFromRepositoryByFile(String id, MyFile file) {
    fileRepository.delete(file);
    logger.debug("the file entity with id: {} is deleted", id);
  }

  private void deleteFileFromStorageServiceFiles(MyFile file) {
    String storageId = file.getStorageId();
    fileStorageService.delete(storageId);
  }

  private MyFile getFileByIdFromRepository(String id) {
    return fileRepository.findById(id).orElseThrow(() -> new FileNotRegisterException(id));
  }
}
