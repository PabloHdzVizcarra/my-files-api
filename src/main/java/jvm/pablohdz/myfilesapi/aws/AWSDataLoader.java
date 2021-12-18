package jvm.pablohdz.myfilesapi.aws;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

@Component
public class AWSDataLoader implements ApplicationListener<ContextRefreshedEvent> {
  private boolean ALREADY_SETUP = false;

  @Value("${aws.queue.name}")
  private String queueName;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (ALREADY_SETUP) return;
    List<String> listQueues = thisQueueExists();

    if (listQueues.size() == 0)
      createQueue(queueName);

    ALREADY_SETUP = true;
  }

  private void createQueue(String queueName) {
    SqsClient sqsClient = createSQSClient();
    CreateQueueRequest queueRequest = CreateQueueRequest.builder().queueName(queueName).build();
    sqsClient.createQueue(queueRequest);
  }

  private List<String> thisQueueExists() {
    try (SqsClient sqsClient = createSQSClient(); ) {
      ListQueuesRequest listQueuesRequest = ListQueuesRequest.builder().queueNamePrefix("").build();
      ListQueuesResponse listQueuesResponse = sqsClient.listQueues(listQueuesRequest);

      return listQueuesResponse.queueUrls();
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  private SqsClient createSQSClient() {
    Region region = Region.US_EAST_2;
    return SqsClient.builder().region(region).build();
  }
}
