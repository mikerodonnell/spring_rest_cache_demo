package demo.chat.serialization;

/**
 * flattened representation of a Message entity to simplify deserialization of an request body.
 */
public class MessageRepresentation {

	protected String messageType;
	protected String senderUsername;
	protected String recipientUsername;
	protected String messageBody;
	
	public String getMessageType() {
		return messageType;
	}
	public void setmessageType(String messageType) {
		this.messageType = messageType;
	}
	
	public String getSenderUsername() {
		return senderUsername;
	}
	public void setSenderUsername(String senderUsername) {
		this.senderUsername = senderUsername;
	}
	
	public String getRecipientUsername() {
		return recipientUsername;
	}
	public void setRecipientUsername(String recipientUsername) {
		this.recipientUsername = recipientUsername;
	}
	
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
}
