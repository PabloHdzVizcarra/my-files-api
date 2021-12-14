package jvm.pablohdz.myfilesapi.service.implementations;

import java.io.IOException;
import jvm.pablohdz.myfilesapi.model.MyFile;
import jvm.pablohdz.myfilesapi.model.User;
import jvm.pablohdz.myfilesapi.repository.MyFileRepository;
import jvm.pablohdz.myfilesapi.service.AuthenticationService;
import jvm.pablohdz.myfilesapi.service.CSVFileStorageService;
import jvm.pablohdz.myfilesapi.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MyCSVService implements CSVService {
  private final CSVFileStorageService csvFileStorageService;
  private final AuthenticationService authenticationService;
  private final MyFileRepository myFileRepository;

  @Autowired
  public MyCSVService(
      CSVFileStorageService csvFileStorageService,
      AuthenticationService authenticationService,
      MyFileRepository myFileRepository) {
    this.csvFileStorageService = csvFileStorageService;
    this.authenticationService = authenticationService;
    this.myFileRepository = myFileRepository;
  }

  @Override
  public void uploadFileCSV(MultipartFile csvFile) {
    byte[] bytes = parseMultipartFileToBytes(csvFile);
    String fileName = getFileName(csvFile);
    User currentUser = authenticationService.getCurrentUser();
    // TODO: 14/12/2021 before creating the file in S3 verify if it has already been registered
    String keyFile = csvFileStorageService.upload(bytes, fileName);
    MyFile myFile = createFile(fileName, currentUser, keyFile);

    myFileRepository.save(myFile);
  }

  private MyFile createFile(String fileName, User currentUser, String keyFile) {
    MyFile myFile = new MyFile();
    myFile.setName(fileName);
    myFile.setStorageId(keyFile);
    myFile.setVersion(1.0);
    myFile.setUser(currentUser);
    return myFile;
  }

  private String getFileName(MultipartFile csvFile) {
    return csvFile.getOriginalFilename();
  }

  private byte[] parseMultipartFileToBytes(MultipartFile csvFile) {
    try {
      return csvFile.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
