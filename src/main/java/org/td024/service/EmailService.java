package org.td024.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {
    @Value("${org.td024.email.api_key}")
    private String apiKey;


    @Value("${org.td024.email.user}")
    private String emailUser;

    public void sendEmail(String to, String subject, String body, boolean isHtml) {
        Email sender = new Email(emailUser);
        Email recipient = new Email(to);
        String type = isHtml ? "text/html" : "text/plain";
        Content content = new Content(type, body);

        Mail mail = new Mail(sender, subject, recipient, content);
        SendGrid sendGrid = new SendGrid(apiKey);
        Request request = new Request();

        new Thread(() -> {
            try {
                request.setMethod(Method.POST);
                request.setEndpoint("mail/send");
                request.setBody(mail.build());

                Response response = sendGrid.api(request);
                int statusCode = response.getStatusCode();
                if (statusCode == 200) {
                    System.out.println("Email Sent Successfully!");
                } else {
                    System.out.println("Email Failed To Send!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
