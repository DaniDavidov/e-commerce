package bg.softuni.ecommerce.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailService(
            JavaMailSender javaMailSender,
            TemplateEngine templateEngine) {

        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendRegistrationEmail(String userEmail, String userName) {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {
            mimeMessageHelper.setFrom("ecommerce@example.com");
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setSubject("Welcome to our website!");
            mimeMessageHelper.setText(generateEmailText(userName), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateEmailText(String userName) {
        Context context = new Context();
        context.setLocale(Locale.getDefault());
        context.setVariable("userName", userName);

        return templateEngine.process("email/registration-email", context);
    }
}
