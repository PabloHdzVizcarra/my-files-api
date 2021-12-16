package jvm.pablohdz.myfilesapi.service.implementations;

import java.io.IOException;
import java.util.Optional;
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

  @Autowired
  public MyCSVService(
      CSVFileStorageService csvFileStorageService,
      AuthenticationService authenticationService,
      MyFileRepository myFileRepository,
      CSVFileMapper csvFileMapper) {
    this.csvFileStorageService = csvFileStorageService;
    this.authenticationService = authenticationService;
    this.myFileRepository = myFileRepository;
    this.csvFileMapper = csvFileMapper;
  }

  @Override
  @Transactional
  public CSVFileDto uploadFileCSV(MultipartFile csvFile) {
    byte[] bytes = parseMultipartFileToBytes(csvFile);
    String fileName = getFileName(csvFile);
    verifyIfFileHasAlreadyRegistered(fileName);

    User currentUser = authenticationService.getCurrentUser();
    String keyFile = csvFileStorageService.upload(bytes, fileName);
    MyFile CSVFile = createFile(fileName, currentUser, keyFile);
    MyFile CSVFileSaved = myFileRepository.save(CSVFile);

    return csvFileMapper.myFileToCSVFileDto(CSVFileSaved);
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
  public FileCSVData update(String id, MultipartFile file) {
    MyFile foundFile = getFileFromRepository(id);
    String storageId = foundFile.getStorageId();
    String currentFileName = getFileName(file);
    InputStreamResource fileUpdated = csvFileStorageService.update(storageId, file);
    foundFile.setName(currentFileName);
    myFileRepository.save(foundFile);
    return new FileCSVData(currentFileName, fileUpdated);
  }

  private void verifyIfFileHasAlreadyRegistered(String fileName) {
    Optional<MyFile> optionalMyFile = myFileRepository.findByName(fileName);
    if (optionalMyFile.isPresent()) throw new CSVFileAlreadyRegisteredException(fileName);
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
   * @param csvFile file in format CSV
   * @return the filename
   */
  private String getFileName(MultipartFile csvFile) {
    return csvFile.getOriginalFilename();
  }

  /**
   * Parse file {@code MultipartFile} to bytes arrays
   *
   * @param csvFile the file to parsing
   * @return array bytes with the data
   */
  private byte[] parseMultipartFileToBytes(MultipartFile csvFile) {
    try {
      return csvFile.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
