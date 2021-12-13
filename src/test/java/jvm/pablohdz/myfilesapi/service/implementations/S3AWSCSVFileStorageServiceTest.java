package jvm.pablohdz.myfilesapi.service.implementations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import jvm.pablohdz.myfilesapi.service.CSVFileStorageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class S3AWSCSVFileStorageServiceTest {
  private CSVFileStorageService csvFileStorageService;

  @BeforeEach
  void setUp() {
    csvFileStorageService = new S3AWSCSVFileStorageService();
    ReflectionTestUtils.setField(csvFileStorageService, "bucketName", "my-files-storage");
    ReflectionTestUtils.setField(csvFileStorageService, "prefixKey", "test/");
  }

  @Test
  void givenFile_whenUpload_thenCreateNewFile() throws IOException {
    // Arrange
    Path path = Path.of("src/main/resources/csv/test.csv");
    byte[] fileBytes = Files.readAllBytes(path);
    // Act
    String keyObjectUploaded = csvFileStorageService.upload(fileBytes);
    // Assert
    Assertions.assertThat(keyObjectUploaded).isInstanceOf(String.class);
  }
}
