package com.sandrimar.backend.services;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import com.sandrimar.backend.dto.TaskResponseDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Value("${mailjet.api.key}")
    private String apiKey;
    @Value("${mailjet.api.secret}")
    private String apiSecret;
    @Value("${mailjet.sender.email}")
    private String senderEmail;
    @Value("${mailjet.sender.name}")
    private String senderName;

    private String recipientEmail;
    private final TaskService taskService;

    public EmailService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Scheduled(cron = "0 30 8 * * *")
    public void sendDailyEmail() {
        if (recipientEmail == null) {
            return;
        }
        List<TaskResponseDTO> pendingTasks = taskService.findPendingTasks();
        if (pendingTasks.isEmpty()) {
            return;
        }
        String emailContent = buildEmailContent(pendingTasks);
        try {
            sendEmail(recipientEmail, "You still have Pending Tasks", emailContent);
        } catch (MailjetException | MailjetSocketTimeoutException e) {
            System.out.println("Error sending email");
        }
    }

    private String buildEmailContent(List<TaskResponseDTO> tasks) {
        StringBuilder content = new StringBuilder();
        content.append("<h3>Pending Tasks Report</h3>");
        content.append("<ul>");

        for (TaskResponseDTO task : tasks) {
            content.append("<br>");
            content.append("<li>");
            content.append("<strong>Title:</strong> ").append(task.title()).append("<br>");
            content.append("<strong>Description:</strong> ").append(task.description()).append("<br>");
            content.append("<strong>Creation Date:</strong> ").append(task.creationDate()).append("<br>");
            content.append("</li>");
        }

        content.append("</ul>");
        return content.toString();
    }

    private void sendEmail(String recipientEmail, String subject, String content) throws MailjetException, MailjetSocketTimeoutException {
        MailjetClient client = new MailjetClient(apiKey, apiSecret, new ClientOptions("v3.1"));

        MailjetRequest request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", senderEmail)
                                        .put("Name", senderName))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", recipientEmail)))
                                .put(Emailv31.Message.SUBJECT, subject)
                                .put(Emailv31.Message.HTMLPART, content)));

        MailjetResponse response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());

    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }
}
