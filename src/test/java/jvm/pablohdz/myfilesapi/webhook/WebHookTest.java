package jvm.pablohdz.myfilesapi.webhook;

import static jvm.pablohdz.myfilesapi.webhook.WebHook.LOCAL_SERVER_PORT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class WebHookTest {
  public static final String FILE_ID = "file_erdf-347jd9";
  public static final String FILENAME = "example.csv";
  public static final List<String> NOTES_EMPTY = List.of();
  private WebHook underTest;
  @Mock private EventPublisher eventPublisher;
  @Mock private Environment environment;

  @BeforeEach
  void setUp() {
    underTest = new WebHook(eventPublisher, environment);
  }

  @Test
  void givenCorrectValues_whenCreateDeleteEvent_thenReturnEvent() {
    when(environment.getProperty(LOCAL_SERVER_PORT)).thenReturn("3000");

    EventHook event = underTest.createDeleteEvent(FILE_ID, FILENAME, NOTES_EMPTY);

    assertThat(event).isNotNull().hasNoNullFieldsOrProperties().isInstanceOf(EventHook.class);
  }
}
