package jvm.pablohdz.myfilesapi.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/api/files")
public class FileResource {

  @PostMapping()
  public String uploadCSVFile(@RequestParam("file") MultipartFile file) {
    return "The user is valid";
  }
}
