package jvm.pablohdz.myfilesapi.service.implementations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import jvm.pablohdz.myfilesapi.dto.CSVFileDataDto;
import jvm.pablohdz.myfilesapi.dto.CSVFileDto;
import jvm.pablohdz.myfilesapi.entity.FileCSVData;
import jvm.pablohdz.myfilesapi.exception.CSVFileAlreadyRegisteredException;
import jvm.pablohdz.myfilesapi.exception.FileCSVNotFoundException;
import jvm.pablohdz.myfilesapi.mapper.CSVFileMapper;
import jvm.pablohdz.myfilesapi.model.MyFile;
import jvm.pablohdz.myfilesapi.model.User;
import jvm.pablohdz.myfilesapi.repository.MyFileRepository;
import jvm.pablohdz.myfilesapi.service.AuthenticationService;
import jvm.pablohdz.myfilesapi.service.CSVFileStorageService;
import jvm.pablohdz.myfilesapi.service.CSVService;
import jvm.pablohdz.myfilesapi.webhook.WebHook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MyCSVService implements CSVService {
  private final CSVFileStorageService csvFileStorageService;
  private final AuthenticationService authenticationService;
  private final MyFileRepository myFileRepository;
  private final CSVFileMapper csvFileMapper;
  private final WebHook webHook;

  @Autowired
  public MyCSVService(
      CSVFileStorageService csvFileStorageService,
      AuthenticationService authenticationService,
      MyFileRepository myFileRepository,
      CSVFileMapper csvFileMapper,
      WebHook webHook) {
    this.csvFileStorageService = csvFileStorageService;
    this.authenticationService = authenticationService;
    this.myFileRepository = myFileRepository;
    this.csvFileMapper = csvFileMapper;
    this.webHook = webHook;
  }

  @Override
  @Transactional
  public CSVFileDto uploadFile(MultipartFile file) {
    byte[] bytes = parseMultipartFileToBytes(file);
    String fileName = getFileName(file);
    verifyIfFileHasAlreadyRegistered(fileName);

    User currentUser = authenticationService.getCurrentUser();
    String keyFile = csvFileStorageService.upload(bytes, fileName, currentUser.getUsername());
    MyFile CSVFile = createFile(fileName, currentUser, keyFile);
    MyFile fileSaved = myFileRepository.save(CSVFile);
    webHook.createEvent(
        "added",
        fileSaved.getId(),
        fileSaved.getName(),
        new ArrayList<>(),
        4,
        "http://localhost:8080/api/files/" + fileSaved.getId());

    return csvFileMapper.myFileToCSVFileDto(fileSaved);
  }

  @Override
  public FileCSVData downloadById(String id) {
    MyFile file = getFileFromRepository(id);
    String storageId = file.getStorageId();
    String fileName = file.getName();
    InputStreamResource data = csvFileStorageService.getFile(storageId);
    return new FileCSVData(fileName, data);
  }

  private MyFile getFileFromRepository(String id) {
    Optional<MyFile> optionalMyFile = myFileRepository.findById(id);
    return optionalMyFile.orElseThrow(() -> new FileCSVNotFoundException(id));
  }

  @Override
  @Transactional
  public CSVFileDataDto update(String id, MultipartFile file) {
    byte[] bytesFromMultipartFile = getBytesFromMultipartFile(file);
    String contentType = file.getContentType();
    String originalFilename = file.getOriginalFilename();
    MyFile foundFile = getFileFromRepository(id);
    String storageId = foundFile.getStorageId();
    csvFileStorageService.update(storageId, bytesFromMultipartFile, originalFilename);
    foundFile.setName(originalFilename);
    myFileRepository.save(foundFile);
    return csvFileMapper.toCSVFileDataDto(originalFilename, contentType, bytesFromMultipartFile);
  }

  private byte[] getBytesFromMultipartFile(MultipartFile file) {
    try {
      return file.getBytes();
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  private void verifyIfFileHasAlreadyRegistered(String filename) {
    Optional<MyFile> optionalMyFile = myFileRepository.findByName(filename);
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
    Collection<MyFile> allFilesByUser = myFileRepository.findAllByUser(user);

    return allFilesByUser.stream()
        .map(csvFileMapper::toCSVFileDto)
        .collect(Collectors.toUnmodifiableList());
  }
}
