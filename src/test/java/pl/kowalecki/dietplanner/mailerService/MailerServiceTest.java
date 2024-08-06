package pl.kowalecki.dietplanner.mailerService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import pl.kowalecki.dietplanner.mailService.MailerService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MailerServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private MailerService mailerService;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mailerService.setEmailFrom("dietplannerg@gmail.com");
    }

    @Test
    @Deprecated
    void testSendEmail() throws MessagingException {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        mailerService.sendEmail("pkowaleckis@gmail.com", "Test Subject", "Test Body");

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    @Deprecated
    void testSendEmailWithException() throws MessagingException {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MessagingException("Error")).when(mailSender).send(any(MimeMessage.class));

        mailerService.sendEmail("pkowaleckis@gmail.com", "Test Subject", "Test Body");

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testSendRegistrationEmail() throws MessagingException {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        mailerService.sendRegistrationEmail("pkowaleckis@gmail.com", "testToken", false);

        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}
