package demo.chat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

// assumption: a message is always between 1 customer user and 1 customer service rep
@Entity
@Table(name="message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="customer_user_id", nullable=false)
	private User customerUser;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="customer_service_user_id", nullable=false)
	private User customerServiceUser;
	
	@Column(name="message_body")
	private String messageBody;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="message_type_id", nullable=false)
	private MessageType messageType;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public User getCustomerUser() {
		return customerUser;
	}
	public void setCustomerUser(User customerUser) {
		this.customerUser = customerUser;
	}
	
	public User getCustomerServiceUser() {
		return customerServiceUser;
	}
	public void setCustomerServiceUser(User customerServiceUser) {
		this.customerServiceUser = customerServiceUser;
	}
	
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	
	public MessageType getMessageType() {
		return messageType;
	}
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

}
