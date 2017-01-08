package demo.chat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="user_type")
public class UserType {

	public static final String CUSTOMER_CODE = "CUSTOMER";
	public static final String CUSTOMER_SERVICE_CODE = "CUSTOMER_SERVICE";
	
	@JsonIgnore // don't expose primary keys outside the application. UserTypes are uniquely identified by #code
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof MessageType))
			return false;
		
		UserType other = (UserType) obj;
		return (code.equalsIgnoreCase(other.code));
	}
}
