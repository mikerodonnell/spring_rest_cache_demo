package demo.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.chat.exception.MessageCreationException;
import demo.chat.exception.UserNotFoundException;
import demo.chat.model.Message;
import demo.chat.serialization.MessageRepresentation;
import demo.chat.service.MessageService;


@Controller
@RequestMapping("/message/")
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> get( @RequestParam String customerUsername, @RequestParam String customerServiceUsername, @RequestParam(required=false) Integer startIndex, @RequestParam(required=false) Integer offset ) {
		ResponseEntity<?> responseEntity = null;
		Iterable<Message> messages = null; // List and Page both extend Iterable
		String errorMessage = null;
		
		try {
			if(startIndex != null && startIndex >= 0 && offset != null && offset > 0)
				messages = messageService.get(customerUsername, customerServiceUsername, startIndex, offset);
			else
				messages = messageService.get(customerUsername, customerServiceUsername);
		}
		catch(UserNotFoundException userNotFoundException) {
			errorMessage = userNotFoundException.getMessage();
		}
		
		if(messages == null) // bad request, one or both of the specified users doesn't exist
			responseEntity = new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST);
		else if(!messages.iterator().hasNext()) // valid request, just no messages found. not an error case
			responseEntity = new ResponseEntity<String>("no chat history between customer " + customerUsername + " and customer service rep " + customerServiceUsername, HttpStatus.OK);
		else
			responseEntity = new ResponseEntity<Iterable<Message>>(messages, HttpStatus.OK);
		
		return responseEntity;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<?> create( @RequestBody MessageRepresentation messageRepresentation ) {
		ResponseEntity<?> responseEntity = null;
		Message message = null;
		String errorMessage = null;
		
		try {
			message = messageService.create(messageRepresentation);
		}
		catch(MessageCreationException messageCreationException) {
			errorMessage = messageCreationException.getMessage();
		}
		catch(UserNotFoundException userNotFoundException) {
			errorMessage = userNotFoundException.getMessage();
		}
		
		if(message == null)
			responseEntity = new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST); // bad request.
		else
			responseEntity = new ResponseEntity<Message>(message, HttpStatus.OK);
		
		return responseEntity;
	}
}
