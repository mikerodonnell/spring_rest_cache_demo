package demo.chat.service;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import demo.chat.dao.UserDao;
import demo.chat.exception.UserCreationException;
import demo.chat.exception.UserNotFoundException;
import demo.chat.model.User;
import demo.chat.model.UserType;
import demo.chat.serialization.UserRepresentation;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations="classpath:/applicationContext-test.xml")
public class UserServiceTest {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserService userService;

	@After
	public void cleanup() {
		userDao.deleteAll(); // clean up any Users a test has persisted so we have a clean slate for the next test
	}

	@Test
	public void testCreateAndGet() {
		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setUsername("kbania");
		userRepresentation.setPassword("gold");
		userRepresentation.setUserType(UserType.CUSTOMER_CODE);

		userService.create(userRepresentation);

		User user = userService.get("kbania");
		assertEquals( UserType.CUSTOMER_CODE, user.getUserType().getCode() );
		assertEquals( "kbania", user.getUsername() );
		assertEquals( "gold", user.getPassword() );
	}

	@Test(expected=UserNotFoundException.class)
	public void testGetNotFound() {
		userService.get("avandalay");
	}

	@Test(expected=UserCreationException.class)
	public void testCreateAlreadyExists() {
		UserRepresentation customerUserRepresentation = new UserRepresentation();
		customerUserRepresentation.setUsername("kbania");
		customerUserRepresentation.setPassword("gold");
		customerUserRepresentation.setUserType(UserType.CUSTOMER_CODE);
		userService.create(customerUserRepresentation);

		// username is a unique identifier for User, regardless of UserType
		UserRepresentation customerServiceUserRepresentation = new UserRepresentation();
		customerServiceUserRepresentation.setUsername("kbania");
		customerServiceUserRepresentation.setPassword("gold");
		customerServiceUserRepresentation.setUserType(UserType.CUSTOMER_CODE);
		userService.create(customerServiceUserRepresentation);
	}

	@Test
	public void testCache() {
		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setUsername("kbania");
		userRepresentation.setPassword("gold");
		userRepresentation.setUserType(UserType.CUSTOMER_CODE);

		// create the user and verify the password is correct when the user is looked up from the DB
		userService.create(userRepresentation);
		assertEquals( "gold", userService.get("kbania").getPassword() );

		// now change this user's password directly in the DAO without telling UserService
		User user = userDao.find("kbania");
		user.setPassword("newPassword");
		userDao.save(user);

		// now look up the user again, and verify that we get the cached password, not the one we set directly with in DAO
		assertEquals( "gold", userService.get("kbania").getPassword() );
	}
}
