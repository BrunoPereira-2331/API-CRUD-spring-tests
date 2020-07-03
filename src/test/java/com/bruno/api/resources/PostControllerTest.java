package com.bruno.api.resources;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.mock;

import java.text.SimpleDateFormat;
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

import com.bruno.api.resources.util.URL;
import com.bruno.api.services.PostService;
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
	public void findPostTest() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		// DateFormat date = DateFormat.getDateInstance();

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
	@DisplayName("Dado um titulo deve retornar uma lista de post")
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
	
}