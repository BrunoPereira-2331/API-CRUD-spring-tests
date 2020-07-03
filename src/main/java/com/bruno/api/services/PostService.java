package com.bruno.api.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruno.api.repositories.PostRepository;
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
		return postRepo.findByTitle(text);
	}
	
	public List<Post> fullSearch(String text, Date minDate, Date maxDate) {
		maxDate = new Date(maxDate.getTime() + 24 * 60 * 60 * 1000);
		return postRepo.fullSearch(text, minDate, maxDate);
	}
}
