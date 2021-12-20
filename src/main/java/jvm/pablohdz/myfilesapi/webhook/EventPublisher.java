package jvm.pablohdz.myfilesapi.webhook;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EventPublisher implements Publisher {
  @Value("${webhook.url}")
  private String triggeredUrl;

  public EventPublisher() {}

  @Override
  public void publish(EventHook event) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

    HttpEntity<EventHook> requestBody = new HttpEntity<>(event, headers);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response =
        restTemplate.exchange(triggeredUrl, HttpMethod.POST, requestBody, String.class);
    HttpStatus statusCode = response.getStatusCode();
    if (!statusCode.is2xxSuccessful()) {
      throw new IllegalStateException("error in the hook server");
    }
  }
}
