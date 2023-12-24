package com.safeway.userservice.service.admin;

import com.safeway.userservice.dto.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);
}