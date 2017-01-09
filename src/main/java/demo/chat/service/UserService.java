package demo.chat.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import demo.chat.dao.UserDao;
import demo.chat.dao.UserTypeDao;
import demo.chat.exception.UserCreationException;
import demo.chat.exception.UserNotFoundException;
import demo.chat.model.User;
import demo.chat.model.UserType;
import demo.chat.serialization.UserRepresentation;

@Service
public class UserService {

	private static final Logger LOGGER = Logger.getLogger(MessageService.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserTypeDao userTypeDao;
	
	
	/**
	 * retrieve the User for the given username.
	 * 
	 * @param username
	 * @throws UserNotFoundException if no user exists with the given username
	 * @return
	 */
	/* upon executing this method once for a given username, cache the User. return the cached User for future calls with the same username is given. since there's
	   no update or delete method for users right now, there's no cache evict/reset logic needed. if null is returned, no caching takes place. so a user can be created
	   at any time without having to worry about the cache. see expiration configuration in ehcache.xml
	*/
	@Cacheable(cacheNames="chatCache", key="#username")
	public User get(final String username) {
		LOGGER.debug("accessing database for user " + username);
		User user = userDao.find(username);
		
		if(user == null)
			throw new UserNotFoundException("no user with username " + username + " exists");
		
		return user;
	}
	
	/**
	 * persist a User based on the given UserRepresentation. if no UserType code is given in the UserRepresentation, default to Customer type.
	 * 
	 * @param userRepresentation
	 * @throws UserRepresentation if a user with the given username already exists.
	 * @return the persistent User entity
	 */
	public User create(final UserRepresentation userRepresentation) {
		try {
			get(userRepresentation.getUsername());
			throw new UserCreationException("user " + userRepresentation.getUsername() + " already exists.");
		}
		catch(UserNotFoundException userNotFoundException) {
			// nothing to do, just verifying that #get() throws a UserCreationException for this username, which means we can proceed to create
		}
		
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
