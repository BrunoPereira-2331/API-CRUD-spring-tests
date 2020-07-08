package com.bruno.config;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.bruno.api.repositories.PostRepository;
import com.bruno.api.repositories.UserRepository;
import com.bruno.domain.model.Post;
import com.bruno.domain.model.User;
import com.bruno.dto.AuthorDTO;
import com.bruno.dto.CommentDTO;

@Configuration
public class Instantiation implements CommandLineRunner {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PostRepository postRepo;

	@Override
	public void run(String... args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		userRepo.deleteAll();
		postRepo.deleteAll();

		User user1 = User.builder().name("Maria db").email("Maria@gmail.com").build();
		User user2 = User.builder().name("Pedro db").email("Pedro@gmail.com").build();
		User user3 = User.builder().name("Bruno aa").email("Bruno@gmail.com").build();

		userRepo.saveAll(Arrays.asList(user1, user2, user3));
		
		Post post1 = Post.builder().date(sdf.parse("23/03/2020")).title("vou viajar").body("vou viajar para sp").author(new AuthorDTO(user1)).build();
		Post post2 = Post.builder().date(sdf.parse("23/03/2020")).title("Bom dia").body("acordei e abri os olhos").author(new AuthorDTO(user1)).build();
		//Post post3 = Post.builder().date(sdf.parse("21/03/2818")).title("").body("").build();
		
		CommentDTO comment1 = new CommentDTO("Boa viagem", sdf.parse("21/03/2018"), new AuthorDTO(user2));
		CommentDTO comment2 = new CommentDTO("Aproveita", sdf.parse("22/03/2018"), new AuthorDTO(user3));
		CommentDTO comment3 = new CommentDTO("sei la", sdf.parse("23/03/2018"), new AuthorDTO(user2));
		
		post1.getComments().addAll(Arrays.asList(comment1, comment2));
		post2.getComments().add(comment3);
		
		
		postRepo.saveAll(Arrays.asList(post1, post2));
		
		user1.getPosts().addAll(Arrays.asList(post1, post2));
		userRepo.save(user1);
	}

}
