package com.evo.trade;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.evo.trade.dao.EvoPaymentDao;
import com.evo.trade.objects.Payment;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://evocoinapp.herokuapp.com", "https://coin.evommo.com", "https://app.evommo.com"})

public class PaymentController {
	
	ObjectMapper mapper = new ObjectMapper();

	@RequestMapping(value = "/api/evo/payments/creater/{createdUserId}", method = RequestMethod.GET, produces = "application/json")
	public List<Payment> getAllPaymentsByCreater(@PathVariable("createdUserId") int createdUserId) throws ClassNotFoundException, SQLException {
		return EvoPaymentDao.getInstance().getAllUserPaymentByCreater(createdUserId);
	}

	@RequestMapping(value = "/api/evo/payments/creater/unconfirmed/{createdUserId}", method = RequestMethod.GET, produces = "application/json")
	public List<Payment> getAllUnconfirmedPaymentsByCreater(@PathVariable("createdUserId") int createdUserId) throws ClassNotFoundException, SQLException {
		return EvoPaymentDao.getInstance().getAllUserUnconfirmedPaymentByCreater(createdUserId);
	}

	@RequestMapping(value = "/api/evo/payments/confirmer/{confirmedUserId}", method = RequestMethod.GET, produces = "application/json")
	public List<Payment> getAllPaymentsByConfirmer(@PathVariable("confirmedUserId") int confirmedUserId) throws ClassNotFoundException, SQLException {
		return EvoPaymentDao.getInstance().getAllUserPaymentByConfirmer(confirmedUserId);
	}

	@RequestMapping(value = "/api/evo/payments/create", method = RequestMethod.POST, produces = "application/json")
	public Payment createPayment(@RequestBody String paymentStr) throws ClassNotFoundException, SQLException, JsonParseException, JsonMappingException, IOException {
		Payment payment = mapper.readValue(paymentStr, Payment.class);
		System.out.println(payment.toString());
		return EvoPaymentDao.getInstance().createPayment(payment);
	}

	@RequestMapping(value = "/api/evo/payments/confirm", method = RequestMethod.POST, produces = "application/json")
	public boolean confirmPayment(@RequestBody String paymentStr) throws ClassNotFoundException, SQLException, JsonParseException, JsonMappingException, IOException {
		Payment payment = mapper.readValue(paymentStr, Payment.class);
		System.out.println(payment.toString());
		return EvoPaymentDao.getInstance().confirmPayment(payment);
	}
}
