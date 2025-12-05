package com.unin.airlines.user_service.service;

public interface MailService {

    void sendSimpleMail(String receiverEmail, String sub, String message);
}
