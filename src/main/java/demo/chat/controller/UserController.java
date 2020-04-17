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
import demo.chat.exception.UserNotFoundException;
import demo.chat.model.User;
import demo.chat.serialization.UserRepresentation;
import demo.chat.service.UserService;

@Controller
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private UserService userService;
	
	/**
	 * retrieve the User for the given username as a GET request attribute.
	 * 
	 * @param username
	 * @return HTTP 200 with User details as JSON upon successful creation, or HTTP 400 with error message as String for failure
	 */
	@RequestMapping(value="/{username}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> get( @PathVariable("username") String username ) {
		ResponseEntity<?> responseEntity;
		User user = null;
		String errorMessage = null;
		
		try {
			user = userService.get(username);
		}
		catch(UserNotFoundException userNotFoundException) {
			errorMessage = userNotFoundException.getMessage();
		}
		if(user == null)
			responseEntity = new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST); // return HTTP 400 bad request, though per strict REST design this would be a 404
		else
			responseEntity = new ResponseEntity<>(user, HttpStatus.OK);
		
		return responseEntity;
	}
	
	/**
	 * create a User from the given PUT request body, as captured in a UserRepresentation.
	 * 
	 * @param userRepresentation
	 * @return HTTP 200 with User details as JSON upon successful creation, or HTTP 400 with error message as String for failure
	 */
	@RequestMapping(value = "/create", method = RequestMethod.PUT)
	public @ResponseBody ResponseEntity<?> create( @RequestBody UserRepresentation userRepresentation ) {
		ResponseEntity<?> responseEntity;
		User user = null;
		String errorMessage = null;
		
		try {
			user = userService.create(userRepresentation);
		}
		catch(UserCreationException userCreationException) {
			errorMessage = userCreationException.getMessage();
		}
		
		if(user == null)
			responseEntity = new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST); // return HTTP 400 bad request, though per strict REST design this would be a 404
		else
			responseEntity = new ResponseEntity<>(user, HttpStatus.OK);
		
		return responseEntity;
	}
}
