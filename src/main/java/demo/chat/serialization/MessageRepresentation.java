package demo.chat.serialization;

/**
 * flattened representation of a Message entity to simplify deserialization of an request body.
 */
public class MessageRepresentation {

	protected String messageType;
	protected String customerUsername;
	protected String customerServiceUsername;
	protected String messageBody;
	
	// TODO: is this representation class needed? can we serialize directly to Message.java?
	public String getMessageType() {
		return messageType;
	}
	public void setmessageType(String messageType) {
		this.messageType = messageType;
	}
	
	public String getCustomerUsername() {
		return customerUsername;
	}
	public void setCustomerUsername(String customerUsername) {
		this.customerUsername = customerUsername;
	}
	
	public String getCustomerServiceUsername() {
		return customerServiceUsername;
	}
	public void setCustomerServiceUsername(String customerServiceUsername) {
		this.customerServiceUsername = customerServiceUsername;
	}
	
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
}
