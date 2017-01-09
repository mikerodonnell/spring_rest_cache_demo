package demo.chat.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import demo.chat.dao.MessageDao;
import demo.chat.dao.MessageMetaDao;
import demo.chat.dao.UserDao;
import demo.chat.dao.UserTypeDao;
import demo.chat.exception.UserNotFoundException;
import demo.chat.model.Message;
import demo.chat.model.MessageType;
import demo.chat.model.User;
import demo.chat.model.UserType;
import demo.chat.serialization.MessageRepresentation;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations="classpath:/applicationContext-test.xml")
public class MessageServiceTest {
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private MessageMetaDao messageMetaDao;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserTypeDao userTypeDao;
	
	private User customerUser;
	private User customerServiceUser;
	
	
	@Before
	public void setup() {
		// do our setup and teardown directly in DAO layer to avoid caching issues
		customerUser = new User();
		customerUser.setUsername("kbania");
		customerUser.setPassword("gold");
		customerUser.setUserType( userTypeDao.find(UserType.CUSTOMER_CODE) );
		customerUser = userDao.save(customerUser);
		
		customerServiceUser = new User();
		customerServiceUser.setUsername("uleo");
		customerServiceUser.setPassword("jeffrey");
		customerServiceUser.setUserType( userTypeDao.find(UserType.CUSTOMER_SERVICE_CODE) );
		customerServiceUser = userDao.save(customerServiceUser);
	}
	
	@After
	public void cleanup() {
		messageMetaDao.deleteAll();
		messageDao.deleteAll(); // clean up any Users and Messages a test has persisted so we have a clean slate for the next test (messages first, Message has fkeys to User)
		userDao.deleteAll();
	}
	
	
	@Test
	public void testCreateAndGet() {
		
		MessageRepresentation messageRepresentation = new MessageRepresentation();
		messageRepresentation.setCustomerUsername("kbania");
		messageRepresentation.setCustomerServiceUsername("uleo");
		messageRepresentation.setMessageBody("https://s-media-cache-ak0.pinimg.com/originals/07/c5/b2/07c5b236ccf2658b37d8061c3327615b.jpg");
		messageRepresentation.setmessageType(MessageType.IMAGE_LINK_CODE);
		
		messageService.create(messageRepresentation);
		
		List<Message> messages = messageService.get("kbania", "uleo");
		assertEquals( 1, messages.size() ); // first make sure there's just the 1 message we created;
		Message message = messages.get(0);
		
		assertEquals( customerUser, message.getCustomerUser() );
		assertEquals( customerServiceUser, message.getCustomerServiceUser() );
		assertEquals( "https://s-media-cache-ak0.pinimg.com/originals/07/c5/b2/07c5b236ccf2658b37d8061c3327615b.jpg", message.getMessageBody() );
		assertEquals( MessageType.IMAGE_LINK_CODE, message.getMessageType().getCode() );
	}
	
	@Test(expected=UserNotFoundException.class)
	public void testCreateUserNotFound() {
		MessageRepresentation messageRepresentation = new MessageRepresentation();
		messageRepresentation.setCustomerUsername("avandalay"); // this user doesn't exist
		messageRepresentation.setCustomerServiceUsername("uleo");
		messageRepresentation.setMessageBody("hello");
		
		messageService.create(messageRepresentation);
	}
}
