package jvm.pablohdz.myfilesapi.service;

import jvm.pablohdz.myfilesapi.model.NotificationEmail;

public interface EmailService {
    void sendEmail(NotificationEmail notificationEmail);
}
