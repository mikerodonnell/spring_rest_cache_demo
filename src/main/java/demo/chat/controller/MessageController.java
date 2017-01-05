package demo.chat.controller;

import java.util.List;

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
import demo.chat.model.Message;
import demo.chat.serialization.MessageRepresentation;
import demo.chat.service.MessageService;


@Controller
@RequestMapping("/message/")
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	
	//@RequestMapping(value="/", method = RequestMethod.GET)
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> get( @RequestParam String customerUsername, @RequestParam String customerServiceUsername ) {
		ResponseEntity<?> responseEntity = null;
		
		List<Message> messages = messageService.get(customerUsername, customerServiceUsername);
		// TODO: not found exception
		responseEntity = new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
		
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
		
		if(message == null)
			responseEntity = new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST); // bad request.
		else
			responseEntity = new ResponseEntity<Message>(message, HttpStatus.OK);
		
		return responseEntity;
	}
}
