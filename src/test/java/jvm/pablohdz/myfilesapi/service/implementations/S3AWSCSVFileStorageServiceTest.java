package jvm.pablohdz.myfilesapi.service.implementations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import jvm.pablohdz.myfilesapi.service.CSVFileStorageService;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class S3AWSCSVFileStorageServiceTest {
  private CSVFileStorageService csvFileStorageService;

  @BeforeEach
  void setUp() {
    csvFileStorageService = new S3AWSCSVFileStorageService();
    ReflectionTestUtils.setField(csvFileStorageService, "bucketName", "my-files-storage");
    ReflectionTestUtils.setField(csvFileStorageService, "prefixKey", "my.files/");
  }

  @Test
  @Disabled
  void givenFile_whenUpload_thenCreateNewFile() throws IOException {
    // Arrange
    Path path = Path.of("src/main/resources/csv/test.csv");
    byte[] fileBytes = Files.readAllBytes(path);
    // Act
    String keyObjectUploaded = csvFileStorageService.upload(fileBytes, "example", "john");
    // Assert
    Assertions.assertThat(keyObjectUploaded).isInstanceOf(String.class);
  }

  @Test
  @Disabled
  void givenCorrectId_whenGetFile_thenReturnFoundFile() {
    // Arrange
    String id = "file_csv/my.files/file.csv_106ef207-6082-4663-995f-fdef4150cac4";
    // Act
    InputStreamResource file = csvFileStorageService.getFile(id);
    // Assert
    assertThat(file).isNotNull().isInstanceOf(InputStreamResource.class);
  }

  @Test
  void getListOfObjectByPrefix() {
    List<String> list = csvFileStorageService.findAllByPrefix("james.java01");

    assertThat(list)
        .asList()
        .isNotEmpty()
        .hasSize(2)
        .isInstanceOf(Collection.class);
  }
}
