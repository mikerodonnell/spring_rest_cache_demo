package demo.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.chat.dao.UserDao;
import demo.chat.dao.UserTypeDao;
import demo.chat.model.User;
import demo.chat.model.UserType;
import demo.chat.serialization.UserRepresentation;


@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserTypeDao userTypeDao;
	
	
	public User get(final String username) {
		return userDao.find(username);
	}
	
	public User create(final UserRepresentation userRepresentation) {
		User user = new User();
		user.setUsername(userRepresentation.getUsername());
		user.setPassword(userRepresentation.getPassword());
		
		if( UserType.CUSTOMER_CODE.equalsIgnoreCase(userRepresentation.getUserType()) )
			user.setUserType( userTypeDao.find(UserType.CUSTOMER_CODE) );
		else
			user.setUserType( userTypeDao.find(UserType.CUSTOMER_SERVICE_CODE) );
		
		return userDao.save(user);
	}
	
}
