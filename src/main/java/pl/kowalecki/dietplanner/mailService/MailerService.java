package pl.kowalecki.dietplanner.mailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import pl.kowalecki.dietplanner.utils.UrlTools;

@Service
public class MailerService {

    @Value("${spring.mail.username}")
    String emailFrom;

    private JavaMailSender mailSender;

    public MailerService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Deprecated
    public void sendEmail(String to, String subject, String body){
        sendEmail(to, subject, body, true, true);
    }

    private void sendEmail(String to, String subject, String body, boolean addFooter, boolean isHtml) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper msgHelper = new MimeMessageHelper(message, isHtml);
            msgHelper.setFrom(emailFrom);
            msgHelper.setTo(to);
            msgHelper.setSubject(subject);
            if (addFooter) {
                message.setText(body + "\n\nThis is an automated message, please do not reply.");
            }
            msgHelper.setText(body, isHtml);
            mailSender.send(message);
            System.out.println("Mail sent");
        }catch (MessagingException e){
            e.printStackTrace();
        }
    }

    public void sendRegistrationEmail(String to, String token, boolean isHtml) {
        String subject = "Registration Confirmation";
        String body = "";
        if (isHtml){
            body="<p>Please confirm your registration by clicking the following link:</p>" +
                    "<p><a href='http://"+ UrlTools.appUrl +"/confirm?token=" + token + "'>Confirm your registration</a></p>";
        }else{
            body="Please confirm your registration by clicking the following link: "+"http://"+ UrlTools.appUrl +"/confirm?token=" + token;
        }
        sendEmail(to, subject, body, true, isHtml);
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }
}
