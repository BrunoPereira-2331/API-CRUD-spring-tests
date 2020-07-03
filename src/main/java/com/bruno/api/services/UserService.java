package com.bruno.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruno.api.repositories.UserRepository;
import com.bruno.api.services.exceptions.ObjectNotFoundException;
import com.bruno.domain.model.User;
import com.bruno.dto.NewUserDTO;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User findById(String id) {
		Optional<User> obj = userRepository.findById(id);
		return userRepository.findById(id).orElseThrow(
				() -> new ObjectNotFoundException("Object Not Found, id:" + id + ", Type:" + User.class.getName()));
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public User insert(User user) {
		return userRepository.save(user);
	}
	
	public User update(User user) {
		User newObj = findById(user.getId());
		updateData(newObj, user);
		return userRepository.save(newObj);
		
	}

	private void updateData(User newObj, User user) {
		newObj.setName(user.getName());
		newObj.setEmail(user.getEmail());
	}

	public void deleteById(String id) {
		findById(id);
		userRepository.deleteById(id);
	} 

	public User fromDto(NewUserDTO newUser) {
		return new User(null, newUser.getName(), newUser.getEmail(), null);
	}

}
