package demo.chat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="message_meta")
public class MessageMeta {

	public static final String WIDTH_KEY = "WIDTH";
	public static final String HEIGHT_KEY = "HEIGHT";
	public static final String LENGTH_KEY = "LENGTH";
	public static final String SOURCE_KEY = "SOURCE";
	
	public MessageMeta() {
		// JPA requires a public default constructor
	}
	
	public MessageMeta(Message message, String key, String value) {
		this.message = message;
		this.key = key;
		this.value = value;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="message_id", nullable=false)
	private Message message;
	
	@Column(name="meta_key") // hibernate or JpaRepository has a bug where the column names are not escaped with ` (tik) quotes. KEY is a SQL keyword, have to name the column something else.
	private String key;
	
	@Column
	private String value;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
