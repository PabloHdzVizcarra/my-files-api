package jvm.pablohdz.myfilesapi.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import jvm.pablohdz.myfilesapi.dto.CSVFileDto;
import jvm.pablohdz.myfilesapi.entity.FileCSVData;
import jvm.pablohdz.myfilesapi.exception.FileCSVNotFoundException;
import jvm.pablohdz.myfilesapi.mapper.CSVFileMapper;
import jvm.pablohdz.myfilesapi.model.MyFile;
import jvm.pablohdz.myfilesapi.model.User;
import jvm.pablohdz.myfilesapi.repository.MyFileRepository;
import jvm.pablohdz.myfilesapi.service.implementations.MyCSVService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class MyCSVServiceTest {
  public static final String FILE_ID = "correct-id-file";
  public static final String STORAGE_ID = "file_csv_id";
  public static final MyFile CSV_FILE = new MyFile(STORAGE_ID);
  public static final InputStreamResource FILE_INPUT_STREAM =
      new InputStreamResource(new ByteArrayInputStream("data".getBytes()));
  private static final String WRONG_FILE_ID = "invalid-id";
  private static final MultipartFile FILE_REQUEST =
      new MockMultipartFile(
          "example", "example.csv", MediaType.MULTIPART_FORM_DATA_VALUE, "example data".getBytes());
  public static final String USER_ID = "us_xxx";
  public static final String USERNAME = "john@terminator";
  public static final User USER = new User(USERNAME);
  public static final MyFile FILE_EMPTY = new MyFile();
  public static final CSVFileDto FILE_DTO = new CSVFileDto();
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

  @Test
  void givenValidId_whenGetAllFilesByUserId_thenReturnCollectionOfFiles() {
    when(authenticationService.getCurrentUser()).thenReturn(USER);
    when(myFileRepository.findAllByUser(USER)).thenReturn(List.of(FILE_EMPTY, FILE_EMPTY));
    when(csvFileMapper.toCSVFileDto(FILE_EMPTY)).thenReturn(FILE_DTO);

    Collection<CSVFileDto> collection = csvService.getAllFilesByUserId(USER_ID);

    assertThat(collection).asList().isNotEmpty().hasSize(2);
  }
}
