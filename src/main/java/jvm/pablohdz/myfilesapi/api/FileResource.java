package jvm.pablohdz.myfilesapi.api;

import jvm.pablohdz.myfilesapi.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/files")
public class FileResource {
  private final CSVService csvService;

  @Autowired
  public FileResource(CSVService csvService) {
    this.csvService = csvService;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public String uploadCSVFile(@RequestParam("file") MultipartFile file) {
    csvService.uploadFileCSV(file);
    return "The user is valid";
  }
}
