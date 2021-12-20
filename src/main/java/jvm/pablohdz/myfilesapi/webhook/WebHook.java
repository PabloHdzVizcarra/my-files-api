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

  public EventHook createAddEvent(
      String id, String filename, Collection<String> notes, String uri) {
    return new EventHook("added", id, filename, notes, uri);
  }

  public void sendEvent(EventHook event) {
    publisher.publish(event);
  }
}
