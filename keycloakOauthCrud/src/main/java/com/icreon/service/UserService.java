package com.icreon.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icreon.dao.UserRepository;
import com.icreon.model.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	public User addNewUsers(User user) {
		return repository.save(user);
	}
	
	public List<User> findAll(){
		return repository.findAll();
	}
	
	public Optional<User> findById(String userId){
		return repository.findById(userId);
	}

	public User updateUser(final String userId, User user) {
		Optional<User> optional = repository.findById(userId);
		if(null != optional) {
			User eUser = optional.get();	
			user.setUserId(eUser.getUserId());
			return repository.save(user);
		}
		return null;
	}

	public void deleteById(String userId) {
		repository.deleteById(userId);
	}
	

}
