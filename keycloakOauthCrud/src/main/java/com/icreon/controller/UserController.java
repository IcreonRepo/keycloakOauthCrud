package com.icreon.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.icreon.model.User;
import com.icreon.service.UserService;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public User addNewUsers(@RequestBody User user) {
        LOGGER.info("Saving user.");
        return userService.addNewUsers(user);
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<User> getAllUsers() {
        LOGGER.info("Getting all users.");
        return userService.findAll();
    }
    
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Optional<User> getUser(@PathVariable String userId) {
    	return userService.findById(userId);
    }
	
	
	@RequestMapping(value = "/{userId}", method = RequestMethod.POST)
	public User updateUser(@RequestBody User user, @PathVariable String userId) {
		return userService.updateUser(userId, user);
	}
	
	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable String userId) {
    	userService.deleteById(userId);
    }
	
}
