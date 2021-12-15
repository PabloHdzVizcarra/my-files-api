package jvm.pablohdz.myfilesapi.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import jvm.pablohdz.myfilesapi.entity.FileCSVData;
import jvm.pablohdz.myfilesapi.exception.FileCSVNotFoundException;
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
  private static final String WRONG_FILE_ID = "invalid-id";
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

    FileCSVData fileCSVData = csvService.downloadById(FILE_ID);

    assertThat(fileCSVData).isNotNull().isInstanceOf(FileCSVData.class);
  }

  @Test
  void given_InvalidId_when_DownloadFile_thenThrownException() {
    when(myFileRepository.findById(WRONG_FILE_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> csvService.downloadById(WRONG_FILE_ID))
        .hasMessageContaining(WRONG_FILE_ID)
        .isInstanceOf(FileCSVNotFoundException.class);
  }
}
