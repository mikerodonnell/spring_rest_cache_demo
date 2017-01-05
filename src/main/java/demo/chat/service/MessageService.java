package demo.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.chat.dao.MessageDao;
import demo.chat.dao.MessageTypeDao;
import demo.chat.model.Message;
import demo.chat.model.MessageType;
import demo.chat.model.User;
import demo.chat.serialization.MessageRepresentation;


@Service
public class MessageService {

	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private MessageTypeDao messageTypeDao;
	
	@Autowired
	private UserService userService;
	
	
	public Message create(final MessageRepresentation messageRepresentation) {
		Message message = new Message();
		message.setMessageBody(messageRepresentation.getMessageBody());
		
		User customerUser = userService.get( messageRepresentation.getCustomerUsername() );
		message.setCustomerUser(customerUser);
		
		User customerServiceUser = userService.get( messageRepresentation.getCustomerServiceUsername() );
		message.setCustomerServiceUser(customerServiceUser);
		
		if( MessageType.IMAGE_LINK_CODE.equalsIgnoreCase(messageRepresentation.getMessageType()) )
			message.setMessageType( messageTypeDao.find(MessageType.IMAGE_LINK_CODE) );
		else if( MessageType.VIDEO_LINK_CODE.equalsIgnoreCase(messageRepresentation.getMessageType()) )
			message.setMessageType( messageTypeDao.find(MessageType.VIDEO_LINK_CODE) );
		else
			message.setMessageType( messageTypeDao.find(MessageType.TEXT_CODE) ); // default to TEXT type if none specified
		
		return messageDao.save(message);
	}
	
	public List<Message> get(final String customerUsername, final String customerServiceUsername) {
		User customerUser = userService.get(customerUsername);
		User customerServiceUser = userService.get(customerServiceUsername);
		
		return get(customerUser, customerServiceUser);
	}
	
	public List<Message> get(final User customerUser, final User customerServiceUser) {
		return messageDao.find(customerUser, customerServiceUser);
	}
}
