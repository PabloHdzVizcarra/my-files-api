package jvm.pablohdz.myfilesapi.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import jvm.pablohdz.myfilesapi.dto.CSVFileDto;
import jvm.pablohdz.myfilesapi.entity.FileCSVData;
import jvm.pablohdz.myfilesapi.exception.FileCSVNotFoundException;
import jvm.pablohdz.myfilesapi.mapper.FileMapper;
import jvm.pablohdz.myfilesapi.model.MyFile;
import jvm.pablohdz.myfilesapi.model.User;
import jvm.pablohdz.myfilesapi.repository.MyFileRepository;
import jvm.pablohdz.myfilesapi.service.implementations.FileServiceCSV;
import jvm.pablohdz.myfilesapi.webhook.EventHook;
import jvm.pablohdz.myfilesapi.webhook.WebHook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class MyCSVServiceTest {
  public static final String FILE_ID = "file_jd782jrg-654hd";
  public static final String STORAGE_ID = "file_csv_id";
  public static final MyFile CSV_FILE = new MyFile(STORAGE_ID);
  public static final InputStreamResource FILE_INPUT_STREAM =
      new InputStreamResource(new ByteArrayInputStream("data".getBytes()));
  private static final String WRONG_FILE_ID = "invalid-id";
  public static final String USER_ID = "us_xxx";
  public static final String USERNAME = "john@terminator";
  public static final User USER = new User(USERNAME);
  public static final MyFile FILE_EMPTY = new MyFile();
  public static final CSVFileDto FILE_DTO = new CSVFileDto();
  public static final String FILENAME = "example.csv";
  public static final MockMultipartFile MOCK_MULTIPART_FILE =
      new MockMultipartFile("test.csv", FILENAME, "csv", "example content".getBytes());
  public static final MyFile FILE_ID_NAME =
      new MyFile("file_ad879d", "example.csv", "s3/bucket/file_hjf83yh-as");
  public static final EventHook EVENT = new EventHook();
  private FileService csvService;
  @Mock
  FileStorageService csvFileStorageService;
  @Mock AuthenticationService authenticationService;
  @Mock MyFileRepository myFileRepository;
  @Mock
  FileMapper csvFileMapper;
  @Mock WebHook webHook;

  @BeforeEach
  void setUp() {
    csvService =
        new FileServiceCSV(
            csvFileStorageService, authenticationService, myFileRepository, csvFileMapper, webHook);
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

  @Test
  void whenUploadFile_thenSendEvent() {
    when(myFileRepository.findByName(FILENAME)).thenReturn(Optional.empty());
    when(authenticationService.getCurrentUser()).thenReturn(USER);
    when(myFileRepository.save(any())).thenReturn(FILE_ID_NAME);
    when(webHook.createAddEvent(any(), any(), any(), any())).thenReturn(EVENT);

    csvService.uploadFile(MOCK_MULTIPART_FILE);

    Mockito.verify(webHook, times(1)).sendEvent(EVENT);
  }

  @Test
  void whenUpdateFile_thenSendEvent() {
    when(myFileRepository.findById(FILE_ID)).thenReturn(Optional.of(FILE_ID_NAME));
    when(webHook.createUpdateEvent(any(), any(), any())).thenReturn(EVENT);

    csvService.update(FILE_ID, MOCK_MULTIPART_FILE);

    Mockito.verify(webHook, times(1)).sendEvent(EVENT);
  }
}
