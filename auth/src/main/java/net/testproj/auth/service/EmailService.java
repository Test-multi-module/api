package net.testproj.auth.service;


import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.UUID;


@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendConfirmationEmail(String to, UUID token) throws MessagingException {
        String confirmationLink =
             "https://testproj-web-dev-b0g5gzgga3c2bjge.canadacentral-01.azurewebsites.net/auth/confirm-email/" + token;

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject("Testproj | Email confirmation");
        helper.setText("Please confirm your email by clicking the link " + confirmationLink, false);
        javaMailSender.send(mimeMessage);
    }
}
