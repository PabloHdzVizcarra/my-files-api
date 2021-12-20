package jvm.pablohdz.myfilesapi.webhook;

import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebHook {
  private final Publisher publisher;

  @Autowired
  public WebHook(EventPublisher publisher) {
    this.publisher = publisher;
  }

  public void createEvent(
      String eventType,
      String fileId,
      String filename,
      Collection<String> notes,
      int size,
      String uri) {
    EventHook event = new EventHook();
    event.setEventType(eventType);
    event.setId(fileId);
    event.setName(filename);
    event.setNotes(notes);
    event.setResourceType("file");
    event.setSize(size);
    event.setUri(uri);

    sendEvent(event);
  }

  private void sendEvent(EventHook event) {
    publisher.publish(event);
  }
}
