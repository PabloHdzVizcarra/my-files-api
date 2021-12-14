package jvm.pablohdz.myfilesapi.aws;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


import java.util.List;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.ListIdentitiesResponse;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SesException;

public class EmailServiceTest {

    @Test
    @Disabled
    void sendEmail() {
        String sender = "seguimosenbibo@gmail.com";
        String recipient = "seguimosenbibo@gmail.com";
        String subject = "testing email";

        Region region = Region.US_EAST_2;
        SesClient client = SesClient.builder()
            .region(region)
            .build();

        String bodyText = "Hello, \r\n" + "See the list of customers";
        String bodyHTML = "<html>" + "<head></head>" + "<body>" + "<h1>Hello!</h1>"
            + "<p> See the list of customers.</p>" + "</body>" + "</html>";

        try {
            Destination destination = Destination.builder()
                .toAddresses(recipient)
                .build();

            Content content = Content.builder()
                .data(bodyHTML)
                .build();

            Content sub = Content.builder()
                .data(subject)
                .build();

            Body body = Body.builder()
                .html(content)
                .build();

            Message message = Message.builder()
                .subject(sub)
                .body(body)
                .build();

            SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(destination)
                .message(message)
                .source(sender)
                .build();

            client.sendEmail(emailRequest);
            client.close();
            System.out.println("message is send");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Test
    @Disabled
    void listIdentities() {
        Region region = Region.US_EAST_2;
        SesClient client = SesClient.builder()
            .region(region)
            .build();


        try {
            ListIdentitiesResponse identitiesResponse = client.listIdentities();
            List<String> identities = identitiesResponse.identities();
            identities
                .forEach(System.out::println);
        } catch (SesException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }

    }
}
