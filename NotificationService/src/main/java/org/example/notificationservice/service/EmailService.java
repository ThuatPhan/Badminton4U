package org.example.notificationservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.example.notificationservice.dto.SendMailEvent;
import org.example.notificationservice.dto.OrderResponse;
import org.example.notificationservice.exception.AppException;
import org.example.notificationservice.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {

    JavaMailSender mailSender;
    TemplateEngine templateEngine;

    @NonFinal
    @Value("${emails.sender_email}")
    String senderEmail;

    @NonFinal
    @Value("${emails.sender_name}")
    String senderName;

    public void sendEmail(SendMailEvent request) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail, senderName);
            helper.setTo(request.getTo());
            helper.setSubject(request.getSubject());

            String htmlContent = generateMailBody(request.getOrder());
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new AppException(ErrorCode.MAIL_SEND_FAILED);
        }
    }

    private String generateMailBody(OrderResponse order) {
        Context context = new Context();
        context.setVariable("order", order);
        return templateEngine.process("confirmation-email", context);
    }
}


