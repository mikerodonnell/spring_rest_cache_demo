package demo.chat.exception;

public class UserCreationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String message;
	
	public UserCreationException( String message ) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
