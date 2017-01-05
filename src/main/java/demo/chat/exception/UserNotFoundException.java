package demo.chat.exception;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String message;
	
	public UserNotFoundException() {}
	
	public UserNotFoundException( String message ) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
