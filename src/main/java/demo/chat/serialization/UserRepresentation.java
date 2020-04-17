package demo.chat.serialization;

/**
 * flattened representation of a User entity to simplify deserialization of an request body.
 */
public class UserRepresentation {

	protected String password;
	protected String username;
	protected String userType;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
}
