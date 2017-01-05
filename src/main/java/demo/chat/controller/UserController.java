package demo.chat.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import demo.chat.exception.UserCreationException;
import demo.chat.model.User;
import demo.chat.serialization.UserRepresentation;
import demo.chat.service.UserService;


@Controller
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	@RequestMapping(value="/{username}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> get( @PathVariable("username") String username ) {
		ResponseEntity<?> responseEntity = null;
		
		User user = userService.get(username);
		// TODO: not found exception
		responseEntity = new ResponseEntity<User>(user, HttpStatus.OK);
		
		return responseEntity;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<?> create( @RequestBody UserRepresentation userRepresentation ) {
		ResponseEntity<?> responseEntity = null;
		User user = null;
		String errorMessage = null;
		
		try {
			user = userService.create(userRepresentation);
		}
		catch(UserCreationException userCreationException) {
			errorMessage = userCreationException.getMessage();
		}
		
		if(user == null)
			responseEntity = new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST); // bad request.
		else
			responseEntity = new ResponseEntity<User>(user, HttpStatus.OK);
		
		return responseEntity;
	}
	
}
