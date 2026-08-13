package net.testproj.auth.services;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

//todo: подумать нужен ли мне аналогичный сервис в апи, стоит ли делать 2 имейл сервиса на 2 модкля для независимости в буду щем или все же сделать 1 майлсендер в апи и все

@Service
@AllArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;

    public void sendEmailVerificationCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email verification code");
        message.setText("Your verification code: " + code);

        mailSender.send(message);
    }
}