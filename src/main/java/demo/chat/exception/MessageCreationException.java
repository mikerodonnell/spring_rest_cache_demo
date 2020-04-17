package demo.chat.exception;

public class MessageCreationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String message;
	
	public MessageCreationException( String message ) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
