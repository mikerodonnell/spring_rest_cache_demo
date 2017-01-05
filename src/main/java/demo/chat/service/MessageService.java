package demo.chat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import demo.chat.dao.MessageDao;
import demo.chat.dao.MessageMetaDao;
import demo.chat.dao.MessageTypeDao;
import demo.chat.model.Message;
import demo.chat.model.MessageMeta;
import demo.chat.model.MessageType;
import demo.chat.model.User;
import demo.chat.serialization.MessageRepresentation;


@Service
public class MessageService {

	// hard-coding metadata values
	private static final String STUB_WIDTH = "151px";
	private static final String STUB_HEIGHT = "151px";
	private static final String STUB_LENGTH = "05:34";
	private static final String STUB_SOURCE = "youtube";
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private MessageMetaDao messageMetaDao;
	
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
			message.setMessageType( messageTypeDao.find(MessageType.TEXT_CODE) ); // default to TEXT type if none specified. no metadata for TEXT messages
		
		// persist our parent entity before persisting child entity to avoid non-transient entity errors. in a more sophisticated implementation, we'd create
		// bidireactional (@OneToMany and @ManyToOne) relationships and cascade upon persist of the parent.
		message = messageDao.save(message);
		
		if( MessageType.IMAGE_LINK_CODE.equalsIgnoreCase(messageRepresentation.getMessageType()) ) {
			messageMetaDao.save( new MessageMeta(message, MessageMeta.WIDTH_KEY, STUB_WIDTH) );
			messageMetaDao.save( new MessageMeta(message, MessageMeta.HEIGHT_KEY, STUB_HEIGHT) );
		}
		else if( MessageType.VIDEO_LINK_CODE.equalsIgnoreCase(messageRepresentation.getMessageType()) ) {
			messageMetaDao.save( new MessageMeta(message, MessageMeta.LENGTH_KEY, STUB_LENGTH) );
			messageMetaDao.save( new MessageMeta(message, MessageMeta.SOURCE_KEY, STUB_SOURCE) );
		}
		
		return message;
	}
	
	public List<Message> get(final String customerUsername, final String customerServiceUsername) {
		User customerUser = userService.get(customerUsername);
		User customerServiceUser = userService.get(customerServiceUsername);
		
		return get(customerUser, customerServiceUser);
	}
	
	public Page<Message> get(final String customerUsername, final String customerServiceUsername, int startIndex, int endIndex) {
		User customerUser = userService.get(customerUsername);
		User customerServiceUser = userService.get(customerServiceUsername);
		
		return get(customerUser, customerServiceUser, startIndex, endIndex);
	}
	
	public List<Message> get(final User customerUser, final User customerServiceUser) {
		return messageDao.find(customerUser, customerServiceUser);
	}
	
	public Page<Message> get(final User customerUser, final User customerServiceUser, int startIndex, int endIndex) {
		return messageDao.findAll( new PageRequest(startIndex, endIndex) );
	}
}
