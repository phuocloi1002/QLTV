package com.example.QLTV.service;

import com.example.QLTV.enity.Student;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailService {
    JavaMailSender mailSender;
    TemplateEngine templateEngine;

    @Async
    public void sendActivationEmail(Student student) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,"UTF-8");

            Context context = new Context();
            context.setVariable("fullname", student.getUser().getFullName());
            context.setVariable("studentCode", student.getStudentCode());
            context.setVariable("activationUrl", "http://localhost:8081/login");

            String htmlContent = templateEngine.process("emails/activation-email", context);

            helper.setFrom("phuocloi100204@gmail.com","Thư Viện Thông Minh");
            helper.setTo(student.getUser().getEmail());
            helper.setSubject("Kích hoạt tài khoản thư viện - " + student.getStudentCode());
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("Email kích hoạt đã gửi thành công tới: {}",student.getUser().getEmail());
        }catch (MessagingException e) {
            log.error("Lỗi MessagingException (SMTP/Cấu hình): {}", e.getMessage());
        } catch (UnsupportedEncodingException e) {
            log.error("Lỗi mã hóa tên người gửi: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi không xác định khi gửi mail: {}", e.getMessage());
        }
    }

    @Async
    public void sendResetPasswordEmail(Student student) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("fullName", student.getUser().getFullName());
            context.setVariable("loginUrl", "http://localhost:8081/login"); // URL thực tế của bạn

            String htmlContent = templateEngine.process("emails/reset-password", context);

            helper.setFrom("phuocloi100204@gmail.com", "Thư Viện Thông Minh PTIT");
            helper.setTo(student.getUser().getEmail());
            helper.setSubject("Đặt lại mật khẩu tài khoản Thư viện");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email reset password đã gửi tới: {}", student.getUser().getEmail());
        } catch (Exception e) {
            log.error("Lỗi gửi mail reset password: {}", e.getMessage());
        }
    }


}
