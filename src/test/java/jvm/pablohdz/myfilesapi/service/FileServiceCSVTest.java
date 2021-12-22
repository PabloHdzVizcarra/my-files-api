package jvm.pablohdz.myfilesapi.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import jvm.pablohdz.myfilesapi.dto.FileDto;
import jvm.pablohdz.myfilesapi.dto.FileServiceDataResponse;
import jvm.pablohdz.myfilesapi.exception.FileInvalidExtension;
import jvm.pablohdz.myfilesapi.exception.FileNotRegisterException;
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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class FileServiceCSVTest {
  public static final String FILE_ID = "file_jd782jrg-654hd";
  public static final String STORAGE_ID = "file_csv_id";
  public static final MyFile CSV_FILE = new MyFile(STORAGE_ID);
  public static final InputStreamResource FILE_INPUT_STREAM =
      new InputStreamResource(new ByteArrayInputStream("data".getBytes()));
  public static final ByteArrayResource RESOURCE_CSV =
      new ByteArrayResource("example data".getBytes());
  private static final String WRONG_FILE_ID = "invalid-id";
  public static final String USER_ID = "us_xxx";
  public static final String USERNAME = "john@terminator";
  public static final User USER = new User(USERNAME);
  public static final MyFile FILE_EMPTY = new MyFile();
  public static final FileDto FILE_DTO = new FileDto();
  public static final String FILENAME = "example.csv";
  public static final MockMultipartFile MOCK_MULTIPART_FILE =
      new MockMultipartFile("test.csv", FILENAME, "csv", "example content".getBytes());
  public static final MockMultipartFile FILE_PNG =
      new MockMultipartFile("image", "image.png", "png", "example content".getBytes());
  public static final MyFile FILE_ID_NAME =
      new MyFile("file_ad879d", "example.csv", "s3/bucket/file_hjf83yh-as");
  public static final EventHook EVENT = new EventHook();
  private FileService underTest;
  @Mock FileStorageService fileStorageService;
  @Mock AuthenticationService authenticationService;
  @Mock MyFileRepository fileRepository;
  @Mock FileMapper fileMapper;
  @Mock WebHook webHook;

  @BeforeEach
  void setUp() {
    underTest =
        new FileServiceCSV(
            fileStorageService, authenticationService, fileRepository, fileMapper, webHook);
  }

  @Test
  void whenReadFileById_thenReturnTheFile() {
    when(fileRepository.findById(FILE_ID)).thenReturn(Optional.of(CSV_FILE));
    when(fileStorageService.getFile(STORAGE_ID)).thenReturn(RESOURCE_CSV);

    FileServiceDataResponse file = underTest.download(FILE_ID);

    assertThat(file.getResource()).isNotNull().isInstanceOf(Resource.class);
  }

  @Test
  void given_InvalidId_when_DownloadFile_thenThrownException() {
    when(fileRepository.findById(WRONG_FILE_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> underTest.download(WRONG_FILE_ID))
        .hasMessageContaining(WRONG_FILE_ID)
        .isInstanceOf(FileNotRegisterException.class);
  }

  @Test
  void givenValidId_whenGetAllFilesByUserId_thenReturnCollectionOfFiles() {
    when(authenticationService.getCurrentUser()).thenReturn(USER);
    when(fileRepository.findAllByUser(USER)).thenReturn(List.of(FILE_EMPTY, FILE_EMPTY));
    when(fileMapper.toCSVFileDto(FILE_EMPTY)).thenReturn(FILE_DTO);

    Collection<FileDto> collection = underTest.getFiles(USER_ID);

    assertThat(collection).asList().isNotEmpty().hasSize(2);
  }

  @Test
  void whenUploadFile_thenSendEvent() {
    when(fileRepository.findByName(FILENAME)).thenReturn(Optional.empty());
    when(authenticationService.getCurrentUser()).thenReturn(USER);
    when(fileRepository.save(any())).thenReturn(FILE_ID_NAME);
    when(webHook.createAddEvent(any(), any(), any(), any())).thenReturn(EVENT);

    underTest.upload(MOCK_MULTIPART_FILE);

    Mockito.verify(webHook, times(1)).sendEvent(EVENT);
  }

  @Test
  void whenUpdateFile_thenSendEvent() {
    when(fileRepository.findById(FILE_ID)).thenReturn(Optional.of(FILE_ID_NAME));
    when(webHook.createUpdateEvent(any(), any(), any())).thenReturn(EVENT);

    underTest.update(FILE_ID, MOCK_MULTIPART_FILE);

    Mockito.verify(webHook, times(1)).sendEvent(EVENT);
  }

  @Test
  void givenValidId_whenDeleteFile() {
    when(fileRepository.findById(FILE_ID)).thenReturn(Optional.of(FILE_ID_NAME));

    assertThatCode(() -> underTest.delete(FILE_ID)).doesNotThrowAnyException();
  }

  @Test
  void givenValidId_whenDownloadFile_thenSendDownloadEvent() {
    when(fileRepository.findById(FILE_ID)).thenReturn(Optional.of(FILE_ID_NAME));
    when(fileStorageService.getFile(FILE_ID_NAME.getStorageId())).thenReturn(RESOURCE_CSV);

    underTest.download(FILE_ID);

    Mockito.verify(webHook, times(1)).createDownloadEvent(any(), any(), any());
    Mockito.verify(webHook, times(1)).sendEvent(any());
  }

  @Test
  void givenFileOtherExtension_whenUpload_thenThrownException() {

    assertThatThrownBy(() -> underTest.upload(FILE_PNG))
        .isInstanceOf(FileInvalidExtension.class)
        .hasMessageContaining(FILE_PNG.getOriginalFilename());
  }
}
