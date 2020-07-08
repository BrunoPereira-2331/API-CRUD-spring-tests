package com.bruno.api.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruno.api.repositories.PostRepository;
import com.bruno.api.resources.exceptions.MissingServletRequestParameterException;
import com.bruno.api.services.exceptions.ObjectNotFoundException;
import com.bruno.domain.model.Post;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepo;
	
	public Post findById(String id) {
		return postRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Object Not Found, id:" + id + ", Type:" + Post.class.getName()));
	}
	
	public List<Post> findByTitle(String text) {
		if(text == null || text.isBlank()) {
			throw new MissingServletRequestParameterException("Missing Parameter text");
		}
		return postRepo.findByTitle(text);
	}
	
	public List<Post> fullSearch(String text, Date minDate, Date maxDate) {
		if(text == null) {
			throw new MissingServletRequestParameterException("Missing Parameter text");
		}
		maxDate = new Date(maxDate.getTime() + 24 * 60 * 60 * 1000);
		return postRepo.fullSearch(text, minDate, maxDate);
	}
}
