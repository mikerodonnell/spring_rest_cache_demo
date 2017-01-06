package demo.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import demo.chat.dao.UserDao;
import demo.chat.dao.UserTypeDao;
import demo.chat.exception.UserNotFoundException;
import demo.chat.model.User;
import demo.chat.model.UserType;
import demo.chat.serialization.UserRepresentation;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserTypeDao userTypeDao;
	
	
	/* upon executing this method once for a given username, cache the User. return the cached User for future calls with the same username is given. since there's
	   no update or delete method for users right now, there's no cache evict/reset logic needed. if null is returned, no caching takes place. so a user can be created
	   at any time without having to worry about the cache. see expiration configuration in ehcache.xml
	*/
	@Cacheable(cacheNames="chatCache", key="#username")
	public User get(final String username) {
		User user = userDao.find(username);
		
		if(user == null)
			throw new UserNotFoundException("no user with username " + username + " exists");
		
		return user;
	}
	
	public User create(final UserRepresentation userRepresentation) {
		User user = new User();
		user.setUsername(userRepresentation.getUsername());
		user.setPassword(userRepresentation.getPassword());
		
		if( UserType.CUSTOMER_SERVICE_CODE.equalsIgnoreCase(userRepresentation.getUserType()) )
			user.setUserType( userTypeDao.find(UserType.CUSTOMER_SERVICE_CODE) );
		else
			user.setUserType( userTypeDao.find(UserType.CUSTOMER_CODE) );
		
		return userDao.save(user);
	}
	
}
