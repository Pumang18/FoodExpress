package com.capgemini.food_express.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

   
    private JavaMailSender mailSender;
    
    @Autowired
    public EmailService(JavaMailSender mailSender) {
		this.mailSender=mailSender;
	}

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP for Password Reset");
        message.setText("Your OTP is: " + otp + "\nIt is valid for 10 minutes.");
        mailSender.send(message);
    }
}
