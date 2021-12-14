package jvm.pablohdz.myfilesapi.api;

import jvm.pablohdz.myfilesapi.dto.CSVFileDto;
import jvm.pablohdz.myfilesapi.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<CSVFileDto> uploadCSVFile(@RequestParam("file") MultipartFile file) {
    CSVFileDto dto = csvService.uploadFileCSV(file);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }
}
