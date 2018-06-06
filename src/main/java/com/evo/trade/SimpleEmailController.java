package com.evo.trade;

import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.evo.trade.dao.EvoUserDao;
import com.evo.trade.objects.User;
import com.evo.trade.utils.Utilities;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://evocoinapp.herokuapp.com", "https://coin.evommo.com", "https://app.evommo.com"})
public class SimpleEmailController {
	@Autowired
	private JavaMailSender sender;
	
	@RequestMapping("/api/evo/simpleemail/payment/{username}")
	@ResponseBody
	String home(@PathVariable("username") String username) {
	    try {
	    	User recipientUser = EvoUserDao.getInstance().getUserByUsername(username);
	    	String content = "<p>" + 
	    			"Thân chào <strong>" + recipientUser.getUsername() + "</strong>," + "<br/>" +
	    			Utilities.PAYMENT_EMAIL_CONTENT + 
	    			Utilities.PAYMENT_EMAIL_SIGNATURE + 
	    			"</p>";
	        sendEmail(recipientUser.getEmail(), content, Utilities.PAYMENT_EMAIL_SUBJECT);
	        return "Email Sent!";
	    }catch(Exception ex) {
	        return "Error in sending email: " + ex;
	    }
	}
	
	private void sendEmail(String recipient, String content, String subject) throws Exception{
	    MimeMessage message = sender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);
	    helper.setTo(recipient);
	    helper.setText(content, true);
	    helper.setSubject(subject);
	    sender.send(message);
	}
}
