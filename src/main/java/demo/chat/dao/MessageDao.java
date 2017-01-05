package demo.chat.dao;

import demo.chat.model.Message;


public interface MessageDao extends Dao<Message> {
	
	@Override
	public Message save(Message message);

}
