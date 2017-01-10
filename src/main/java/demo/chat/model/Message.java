package demo.chat.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

// assumption: a message is always between 1 customer user and 1 customer service rep
@Entity
@Table(name="message")
public class Message {

	// don't expose primary keys outside the application. Message doesn't have a unique identifier right now, a GUID field could be created if needed.
	// we're relying on timestamp to ultimately determine equality right now. so if a user sends another user "hello" twice in the same millisecond, our
	// #equals() implementation wouldn't be able to differentiate.
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column
	private Date timestamp;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="sender_id", nullable=false)
	private User sender;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="recipient_id", nullable=false)
	private User recipient;
	
	@Column(name="message_body")
	private String messageBody;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="message_type_id", nullable=false)
	private MessageType messageType;
	
	
	/**
	 * intercept persist of a Message and set the timestamp. we default the timestamp to now() at the DB level, at least in mysql, but this seems more
	 * platform agnostic.
	 */
	@PrePersist
	void setCurrentTimestamp() {
		setTimestamp(new Date());
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	
	public User getRecipient() {
		return recipient;
	}
	public void setRecipient(User recipient) {
		this.recipient = recipient;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((recipient == null) ? 0 : recipient.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		result = prime * result + ((messageBody == null) ? 0 : messageBody.hashCode());
		result = prime * result + ((messageType == null) ? 0 : messageType.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Message))
			return false;
		Message other = (Message) obj;
		if (!recipient.equals(other.recipient))
			return false;
		if (!sender.equals(other.sender))
			return false;
		if (!messageBody.equals(other.messageBody))
			return false;
		if (!messageType.equals(other.messageType))
			return false;
		if (!timestamp.equals(other.timestamp))
			return false;
		
		return true;
	}

}
