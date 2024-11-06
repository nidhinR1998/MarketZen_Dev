package com.nidhin.marketzen.services;

import com.nidhin.marketzen.models.User;
import com.nidhin.marketzen.repository.UserRepository;
import com.nidhin.marketzen.utils.Constants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailService {
    @Autowired
    private UserRepository userRepository;

    private JavaMailSender javaMailSender;
    @Autowired
    private JavaMailSenderImpl mailSender;

    public void sendVarificationOtpEmail(String email, String otp) throws MessagingException {

        User user = userRepository.findByEmail(email);
        String htmlContent = Constants.EMAIL_TEMPLATE_RESET_PASSWORD
                .replace("{fullname}", user.getFullName())
                .replace("{otp}", String.valueOf(otp));

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            if (isEmailValid(user.getEmail())) {
                helper.setTo(user.getEmail());
                helper.setSubject("Password Reset Request");
                helper.setText(htmlContent, true);
                mailSender.send(mimeMessage);
                //     logger.debug("Password reset code generated and email sent to user: {}", username)
            }
        } catch (MessagingException ex) {
            //   logger.error("Failed to send email", ex);
        }
    }

    public boolean isEmailValid(String email) {
        String emailRegex = Constants.VALIED_EMAIL;
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}