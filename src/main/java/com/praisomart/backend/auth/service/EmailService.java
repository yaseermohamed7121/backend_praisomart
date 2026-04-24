package com.praisomart.backend.auth.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendOtpEmail(String toEmail, String otp, String purpose) {

        String subject = "Praisomart - OTP Verification";
        String html = buildTemplate(otp, purpose);

        sendHtml(toEmail, subject, html);
    }

    private String buildTemplate(String otp, String purpose) {

        return """
            <html>
            <body style="font-family:Arial;background:#f4f4f4;padding:20px;">
                <div style="max-width:500px;margin:auto;background:#fff;padding:20px;border-radius:10px;">

                    <h2 style="color:#2e6cff;text-align:center;">Praisomart</h2>

                    <p>Hello 👋</p>

                    <p>You requested OTP for: <b>%s</b></p>

                    <div style="text-align:center;margin:20px 0;">
                        <h1 style="letter-spacing:5px;">%s</h1>
                    </div>

                    <p><b>Valid for 5 minutes</b></p>

                    <p style="color:red;"><b>Do not share this OTP</b></p>

                    <hr/>

                    <p style="font-size:12px;color:gray;">
                        Automated message from Praisomart
                    </p>
                </div>
            </body>
            </html>
            """.formatted(purpose, otp);
    }

    private void sendHtml(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Email sending failed");
        }
    }
}