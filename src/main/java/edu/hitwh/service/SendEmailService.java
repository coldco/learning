package edu.hitwh.service;

import edu.hitwh.response.EmailSendResponse;
import edu.hitwh.response.ResultResponse;
import edu.hitwh.request.SendEmailRequest;

import java.time.LocalDateTime;

public interface SendEmailService {
    public ResultResponse<EmailSendResponse> sendEmail(SendEmailRequest sendEmailRequest, Short visibility);

    void sendRandomEmail(String title, String content);
    ResultResponse<EmailSendResponse> sendScheduledEmail(LocalDateTime sendTime, SendEmailRequest sendEmailRequest);
}
