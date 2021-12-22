package jvm.pablohdz.myfilesapi.webhook;

import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class WebHook {
  public static final String LOCAL_SERVER_PORT = "local.server.port";
  private final Publisher publisher;
  private final Environment environment;

  @Autowired
  public WebHook(EventPublisher publisher, Environment environment) {
    this.publisher = publisher;
    this.environment = environment;
  }

  public EventHook createAddEvent(
      String id, String filename, Collection<String> notes, String uri) {
    return new EventHook(TypeEvent.ADDED, id, filename, notes, uri);
  }

  public void sendEvent(EventHook event) {
    publisher.publish(event);
  }

  public EventHook createUpdateEvent(String fileId, String filename, Collection<String> notes) {
    return new EventHook(TypeEvent.UPDATE, fileId, filename, notes, createUriToFile(fileId));
  }

  private String createUriToFile(String id) {
    String host;
    try {
      host = Inet6Address.getLocalHost().getHostAddress();
      String port = environment.getProperty(LOCAL_SERVER_PORT);
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

  public EventHook createDeleteEvent(String fileId, String filename, Collection<String> notes) {
    return new EventHook(TypeEvent.DELETE, fileId, filename, notes, createUriToFile(fileId));
  }

  public EventHook createDownloadEvent(String fileId, String filename, List<String> notes) {
    return new EventHook(TypeEvent.DELETE, fileId, filename, notes, createUriToFile(fileId));
  }
}
