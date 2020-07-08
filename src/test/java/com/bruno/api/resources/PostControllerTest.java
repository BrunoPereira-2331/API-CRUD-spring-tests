package com.bruno.api.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bruno.api.services.PostService;
import com.bruno.api.services.exceptions.ObjectNotFoundException;
import com.bruno.domain.model.Post;
import com.bruno.dto.AuthorDTO;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc
public class PostControllerTest {

	String POST_URI = "/posts";

	@MockBean
	private PostService postService;

	@Autowired
	MockMvc mvc;

	@Test
	@DisplayName("Deve retornar um post")
	public void findByIdTest() throws Exception {
		String id = "abc";

		AuthorDTO author = new AuthorDTO();
		author.setId(id);
		author.setName("bruno");
		Post post = Post.builder().id(id).date(new Date()).title("oi").body("tchau").author(author).build();

		BDDMockito.when(postService.findById(Mockito.anyString())).thenReturn(post);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(POST_URI + "/" + id);

		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("date").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("title").value(post.getTitle()))
				.andExpect(MockMvcResultMatchers.jsonPath("body").value(post.getBody()))
				.andExpect(MockMvcResultMatchers.jsonPath("author").isNotEmpty());
	}
	
	@Test
	@DisplayName("Dado um titulo deve retornar uma lista de posts")
	public void findByTitleTest() throws Exception {
		String text = "Bom%20Dia";
		String id = "abc";
		
		AuthorDTO author = new AuthorDTO();
		author.setId(id);
		author.setName("bruno");
		
		List<Post> posts = new ArrayList<>();
		posts.add(Post.builder().id(id).date(new Date()).title("Bom Dia").body("tchau").author(author).build());
		
		BDDMockito.when(postService.findByTitle(Mockito.anyString())).thenReturn(posts);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(POST_URI + "/titlesearch?text=" + text)
				.accept(MediaType.APPLICATION_JSON);
		
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0]").isNotEmpty());
	}
	
	@Test
	@DisplayName("Dado um texto, data minima & data maxima deve retornar uma lista de posts")
	public void fullSearchTest() throws Exception {
		String id = "abc";
		String text = "Bom%20Dia";
		String minDate = "2020-01-10";
		String maxDate = "2020-02-10";
		
		AuthorDTO author = new AuthorDTO();
		author.setId(id);
		author.setName("bruno");
		
		List<Post> posts = new ArrayList<>();
		posts.add(Post.builder().id(id).date(new Date()).title("Bom Dia").body("tchau").author(author).build());
		posts.add(Post.builder().id(id).date(new Date()).title("Boa noite").body("tchau dnv").author(author).build());
		
		BDDMockito.when(postService.fullSearch(Mockito.anyString(), Mockito.any(Date.class), Mockito.any(Date.class))).thenReturn(posts);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(POST_URI + "/fullsearch?text=" + text + "&minDate=" + minDate + "&maxDate=" + maxDate).accept(MediaType.APPLICATION_JSON);
	
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$[0]").isNotEmpty())
			.andExpect(MockMvcResultMatchers.jsonPath("$[1]").isNotEmpty());
	}
	
	@Test
	@DisplayName("Deve retornar exception ao passar um id invalido para buscar um post")
	public void findByIdInvalid() throws Exception {
		String id = "";
		
		BDDMockito.when(postService.findById(id)).thenThrow(ObjectNotFoundException.class);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(POST_URI + "/" + id).accept(MediaType.APPLICATION_JSON);
		
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound());
		
	}
	
	
	
}