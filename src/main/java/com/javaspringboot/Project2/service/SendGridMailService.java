package com.javaspringboot.Project2.service;

import com.sendgrid.Content;

public interface SendGridMailService {
    void sendMail(String subject, Content content, String sendTo);
}
