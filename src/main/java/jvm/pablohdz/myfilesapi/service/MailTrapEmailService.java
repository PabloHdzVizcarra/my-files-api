package jvm.pablohdz.myfilesapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import jvm.pablohdz.myfilesapi.model.NotificationEmail;

@Service
public class MailTrapEmailService implements EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public MailTrapEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("spring.files.service@gmail.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(notificationEmail.getBody());
        };

        try {
            mailSender.send(messagePreparator);
        } catch (MailException mailException) {
            throw new IllegalStateException("exception occurred when try send mail to: " +
                notificationEmail.getRecipient());
        }
    }
}
