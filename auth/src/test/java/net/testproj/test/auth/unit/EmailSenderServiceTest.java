package net.testproj.test.auth.unit;

import net.testproj.auth.services.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EmailSenderServiceTest {

    @Test
    void sendsVerificationCodeToRequestedEmail() {
        JavaMailSender mailSender = mock(JavaMailSender.class);
        EmailSenderService service = new EmailSenderService(mailSender);
        String email = "user@example.com";
        String code = "123456";

        service.sendEmailVerificationCode(email, code);

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(messageCaptor.capture());
        SimpleMailMessage message = messageCaptor.getValue();
        assertArrayEquals(new String[]{email}, message.getTo());
        assertEquals("Email verification code", message.getSubject());
        assertEquals("Your verification code: " + code, message.getText());
    }
}
