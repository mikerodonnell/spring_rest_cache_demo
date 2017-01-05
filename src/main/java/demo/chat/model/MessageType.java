package demo.chat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="message_type")
public class MessageType {
	
	public static final String TEXT_CODE = "TEXT";
	public static final String IMAGE_LINK_CODE = "IMAGE_LINK";
	public static final String VIDEO_LINK_CODE = "VIDEO_LINK";
	
	@JsonIgnore // don't expose primary keys outside the application. MessageTypes are uniquely identified by #code
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column
	private String code;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
