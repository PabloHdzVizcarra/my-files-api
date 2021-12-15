package jvm.pablohdz.myfilesapi.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssumptions.given;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import jvm.pablohdz.myfilesapi.mapper.CSVFileMapper;
import jvm.pablohdz.myfilesapi.model.MyFile;
import jvm.pablohdz.myfilesapi.repository.MyFileRepository;
import jvm.pablohdz.myfilesapi.service.implementations.MyCSVService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;

@ExtendWith(MockitoExtension.class)
class MyCSVServiceTest {
  public static final String FILE_ID = "correct-id-file";
  public static final String STORAGE_ID = "file_csv_id";
  public static final MyFile CSV_FILE = new MyFile(STORAGE_ID);
  public static final InputStreamResource FILE_INPUT_STREAM =
      new InputStreamResource(new ByteArrayInputStream("data".getBytes()));
  private CSVService csvService;
  @Mock CSVFileStorageService csvFileStorageService;
  @Mock AuthenticationService authenticationService;
  @Mock MyFileRepository myFileRepository;
  @Mock CSVFileMapper csvFileMapper;

  @BeforeEach
  void setUp() {
    csvService =
        new MyCSVService(
            csvFileStorageService, authenticationService, myFileRepository, csvFileMapper);
  }

  @Test
  void whenReadFileById_thenReturnTheFile() {
    when(myFileRepository.findById(FILE_ID)).thenReturn(Optional.of(CSV_FILE));
    when(csvFileStorageService.getFile(STORAGE_ID)).thenReturn(FILE_INPUT_STREAM);

    InputStreamResource fileCSV = csvService.downloadById(FILE_ID);

    assertThat(fileCSV).isNotNull().isInstanceOf(InputStreamResource.class);
  }
}
