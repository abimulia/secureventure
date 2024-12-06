/**
 * EmailUtils.java
 * 06-Dec-2024
 */
package com.abimulia.secureventure.utils;

import static com.abimulia.secureventure.enums.VerificationType.ACCOUNT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author abimu
 *
 * @version 1.0 (06-Dec-2024)
 * @since 06-Dec-2024 12:38:08 AM
 * 
 * 
 * Copyright(c) 2024 Abi Mulia
 */
@Component
@Slf4j
public class EmailUtils {
	
	@Autowired
	private JavaMailSender mailSender;

	
	@Async
	public Boolean sendVerificationUrl(String firstName, String email, String verUrl, String verType) {
		log.debug("sendVerificationUrl() for {} with email {} and verUrl {} ", firstName, email, verUrl );
		Boolean result = false;
		SimpleMailMessage msg = new SimpleMailMessage();
		log.debug("msg: " + msg);
		String subjectMessage = "Password verification";
		log.debug("subjectMessage: " + subjectMessage);
		String greetingMessage = "Here's your reset password URL: ";
		
		if (verType.equalsIgnoreCase(ACCOUNT.getType())) {
			subjectMessage = "Account verification";
			greetingMessage = "Thank you for registering, please verify your account to continue: ";
		}
		log.debug("greetingMessage: " + greetingMessage);
		msg.setTo(email);
		msg.setFrom("noreply@trial-3z0vklo96y7l7qrx.mlsender.net");
		msg.setSubject(subjectMessage);
		msg.setText("Dear "+firstName+","+System.lineSeparator()+System.lineSeparator()+
				greetingMessage + System.lineSeparator()+
				verUrl+ System.lineSeparator()+
				System.lineSeparator()+
				"Thank You");
		log.debug("msg: " + msg);
		try {
			log.debug("sending email now !!!");
			mailSender.send(msg);
			log.info("Email for user {} with email {} sent.", firstName,email);
			result = true;
		} catch (Exception e) {
			log.error("Email for user {} with email {} failed, {}", firstName,email,e.getMessage());
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	

}
