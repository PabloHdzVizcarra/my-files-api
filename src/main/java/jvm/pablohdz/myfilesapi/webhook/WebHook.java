package jvm.pablohdz.myfilesapi.webhook;

import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class WebHook {
  private final Publisher publisher;
  private final Environment environment;

  @Autowired
  public WebHook(EventPublisher publisher, Environment environment) {
    this.publisher = publisher;
    this.environment = environment;
  }

  public EventHook createAddEvent(
      String id, String filename, Collection<String> notes, String uri) {
    return new EventHook("added", id, filename, notes, uri);
  }

  public void sendEvent(EventHook event) {
    publisher.publish(event);
  }

  public EventHook createUpdateEvent(String fileId, String filename, Collection<String> notes) {
    return new EventHook("update", fileId, filename, notes, createUriToFile(fileId));
  }

  private String createUriToFile(String id) {
    String host;
    try {
      host = Inet6Address.getLocalHost().getHostAddress();
      String port = environment.getProperty("local.server.port");
      return UriComponentsBuilder.newInstance()
          .scheme("http")
          .host(host + ":" + port)
          .path("/api/files/" + id)
          .build()
          .toString();
    } catch (UnknownHostException e) {
      e.printStackTrace();
      throw new IllegalStateException(e.getMessage());
    }
  }
}
