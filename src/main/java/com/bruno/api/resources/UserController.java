package com.bruno.api.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bruno.api.services.UserService;
import com.bruno.domain.model.Post;
import com.bruno.domain.model.User;
import com.bruno.dto.NewUserDTO;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<NewUserDTO> findById(@PathVariable String id) {
		User user = userService.findById(id);
		return ResponseEntity.ok().body(new NewUserDTO(user));
	}

	@GetMapping
	public ResponseEntity<List<NewUserDTO>> findAll() {
		List<User> listUsers = userService.findAll();
		List<NewUserDTO> listDto = listUsers.stream().map(obj -> new NewUserDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);

	}

	@PostMapping
	public ResponseEntity<Void> insert(@RequestBody NewUserDTO newUserDto) {
		User user = userService.fromDto(newUserDto);
		user = userService.insert(user);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).build();

	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@RequestBody NewUserDTO userDto, @PathVariable String id) {
		User user = userService.fromDto(userDto);
		user.setId(id);
		user = userService.update(user);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteById(@PathVariable String id) {
		userService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping(value = "/{id}/posts")
	public ResponseEntity<List<Post>> findPosts(@PathVariable String id) {
		User user = userService.findById(id);
		return ResponseEntity.ok().body(user.getPosts());
		
		
	}
	
}
