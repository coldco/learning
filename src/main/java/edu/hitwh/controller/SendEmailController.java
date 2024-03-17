package edu.hitwh.controller;

import edu.hitwh.response.EmailSendResponse;
import edu.hitwh.response.ResultResponse;
import edu.hitwh.request.SendEmailRequest;
import edu.hitwh.service.SendEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class SendEmailController {
    private final SendEmailService sendEmailService;

    @Operation(summary = "发送一般信件")
    @PostMapping("/sendEmail")
    public ResultResponse<EmailSendResponse> sendEmail(@RequestBody SendEmailRequest sendEmailRequest){
        return sendEmailService.sendEmail(sendEmailRequest,(short)0);
    }

    @Operation(summary = "发送延时邮件")
    @PostMapping("/sendScheduledEmail")
    public ResultResponse<EmailSendResponse> sendScheduledEmail(@Parameter(description = "发件时间") @RequestParam @DateTimeFormat(pattern = "yyyy-M-d H:m:s")LocalDateTime sendTime,
                                                                @RequestBody SendEmailRequest sendEmailRequest){

       return sendEmailService.sendScheduledEmail(sendTime,sendEmailRequest);
    }

    @Operation(summary = "随机发件")
    @PostMapping("/sendRandomEmail")
    public ResultResponse<Object> sendRandomEmail(@RequestBody SendEmailRequest sendEmailRequest){
        sendEmailService.sendRandomEmail(sendEmailRequest.getTitle(),sendEmailRequest.getContent());
        return ResultResponse.success();}
}
