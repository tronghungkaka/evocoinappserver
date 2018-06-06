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

import com.evo.trade.dao.EvoUserDao;
import com.evo.trade.objects.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://evocoinapp.herokuapp.com", "https://coin.evommo.com", "https://app.evommo.com"})

public class UsersController {
	
	ObjectMapper mapper = new ObjectMapper();

	@RequestMapping(value = "/api/evo/users", method = RequestMethod.GET, produces = "application/json")
	public List<User> getAllUsers() throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().getAllUsers();
	}

	@RequestMapping(value = "/api/evo/users/account", method = RequestMethod.GET, produces = "application/json")
	public List<User> getAllUsersAccount() throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().getAllUsersAccount();
	}
	
	@RequestMapping(value = "/api/evo/users/id/{id}", method = RequestMethod.GET, produces = "application/json")
	public User getUserById(@PathVariable("id") int id) throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().getUserById(id);
	}

	@RequestMapping(value = "/api/evo/users/username/{username}", method = RequestMethod.GET, produces = "application/json")
	public User getUserByUsername(@PathVariable("username") String username) throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().getUserByUsername(username);
	}

	@RequestMapping(value = "/api/evo/users/transactionId/{transactionId}", method = RequestMethod.GET, produces = "application/json")
	public User getUserByTransactionId(@PathVariable("transactionId") String transactionId) throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().getUserByTransactionId(transactionId);
	}
	
	@RequestMapping(value = "/api/evo/users", method = RequestMethod.POST, produces = "application/json")
	public User createUser(@RequestBody String userStr) throws ClassNotFoundException, SQLException, JsonParseException, JsonMappingException, IOException {
		User user = mapper.readValue(userStr, User.class);
		System.out.println(user.toString());
		return EvoUserDao.getInstance().createUser(user);
	}
	
	@RequestMapping(value = "/api/evo/users/{username}", method = RequestMethod.PUT, produces = "application/json")
	public boolean updateUser(@PathVariable("username") String username,
			@RequestBody String userStr) throws ClassNotFoundException, SQLException, JsonParseException, JsonMappingException, IOException {
		User user = mapper.readValue(userStr, User.class);
		System.out.println(user.toString());
		return EvoUserDao.getInstance().updateUser(username, user);
	}

	@RequestMapping(value = "/api/evo/users/create/payment", method = RequestMethod.PUT, produces = "application/json")
	public boolean createPaymentUser(@RequestBody String userStr) throws ClassNotFoundException, SQLException, JsonParseException, JsonMappingException, IOException {
		User user = mapper.readValue(userStr, User.class);
		System.out.println(user.toString());
		return EvoUserDao.getInstance().createPaymentUser(user);
	}

	@RequestMapping(value = "/api/evo/users/confirm/payment", method = RequestMethod.PUT, produces = "application/json")
	public boolean confirmPaymentUser(@RequestBody String userStr) throws ClassNotFoundException, SQLException, JsonParseException, JsonMappingException, IOException {
		User user = mapper.readValue(userStr, User.class);
		System.out.println(user.toString());
		return EvoUserDao.getInstance().confirmPaymentUser(user);
	}
	
	@RequestMapping(value = "/api/evo/users/change/role", method = RequestMethod.PUT, produces = "application/json")
	public boolean changeRole(@RequestBody String userStr) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException, SQLException {
		User user = mapper.readValue(userStr, User.class);
		System.out.println(user.toString());
		return EvoUserDao.getInstance().changeRole(user);
	}

	@RequestMapping(value = "/api/evo/users/change/password", method = RequestMethod.PUT, produces = "application/json")
	public boolean changePassword(@RequestBody String userStr) throws JsonParseException, JsonMappingException, IOException, ClassNotFoundException, SQLException {
		User user = mapper.readValue(userStr, User.class);
		System.out.println(user.toString());
		return EvoUserDao.getInstance().changePassword(user);
	}
	
	@RequestMapping(value = "/api/evo/users/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public boolean deleteUser(@PathVariable("id") int id) throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().deleteUser(id);
	}
	
	@RequestMapping(value = "/api/evo/authenticate", method = RequestMethod.POST, produces = "application/json")
	public String authenticateUser(@RequestBody  String userStr) throws ClassNotFoundException, SQLException, JsonParseException, JsonMappingException, IOException {
		User user = mapper.readValue(userStr, User.class);
		String res = mapper.writeValueAsString(EvoUserDao.getInstance().authenticateUser(user));
		System.out.println("res=" + res);
		return res;
	}
	
	@RequestMapping(value = "/api/evo/users/validate/username/{username}", method = RequestMethod.GET, produces = "application/json")
	public boolean validateUsername(@PathVariable("username") String username) throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().validateUsername(username);
	}

	@RequestMapping(value = "/api/evo/users/validate/email/{email}", method = RequestMethod.GET, produces = "application/json")
	public boolean validateEmail(@PathVariable("email") String email) throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().validateEmail(email);
	}

	@RequestMapping(value = "/api/evo/users/validate/phone/{phone}", method = RequestMethod.GET, produces = "application/json")
	public boolean validatePhone(@PathVariable("phone") String phone) throws ClassNotFoundException, SQLException {
		return EvoUserDao.getInstance().validatePhone(phone);
	}
}
