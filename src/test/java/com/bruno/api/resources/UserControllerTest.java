package com.bruno.api.resources;

import static org.hamcrest.CoreMatchers.containsString;

import java.util.Arrays;
import java.util.Date;

import org.hamcrest.Matchers;
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

import com.bruno.api.services.UserService;
import com.bruno.api.services.exceptions.ObjectNotFoundException;
import com.bruno.domain.model.Post;
import com.bruno.domain.model.User;
import com.bruno.dto.AuthorDTO;
import com.bruno.dto.CommentDTO;
import com.bruno.dto.NewUserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

	static String USER_URI = "/users";

	@MockBean
	UserService userService;

	@Autowired
	MockMvc mvc;

	@Test
	@DisplayName("Deve retornar um usuario")
	public void findByIdTest() throws Exception {
		String id = "asdasdsaasd";
		User user = User.builder().id(id).name("Bruno").email("bruno@gmail.com").build();

		BDDMockito.given(userService.findById(id)).willReturn(user);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(USER_URI + "/" + id)
				.accept(MediaType.APPLICATION_JSON);

		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("name").value(user.getName()))
				.andExpect(MockMvcResultMatchers.jsonPath("email").value(user.getEmail()));
	}

	@Test
	@DisplayName("Deve retornar uma lista de usuarios")
	public void findAllUsersTest() throws Exception {
		String nameUser1 = "Bruno";
		String emailUser1 = "bruno@gmail.com";

		String nameUser2 = "Pedro";
		String emailUser2 = "pedro@gmail.com";

		User user = User.builder().id("abcde").name(nameUser1).email(emailUser1).build();
		User user2 = User.builder().id("abcde").name(nameUser2).email(emailUser2).build();

		BDDMockito.given(userService.findAll()).willReturn(Arrays.asList(user, user2));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(USER_URI).accept(MediaType.APPLICATION_JSON);

		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0]").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$[1]").isNotEmpty());

		Mockito.verify(userService, Mockito.times(1)).findAll();

	}

	@Test
	@DisplayName("Deve criar um usuario")
	public void insertUserTest() throws Exception {
		NewUserDTO newUserDto = new NewUserDTO();
		newUserDto.setName("Bruno");
		newUserDto.setEmail("bruno@hotmail.com");
		
		User savedUser = new User();
		savedUser.setId("abcd");
		savedUser.setName("Bruno");
		savedUser.setEmail("bruno@hotmail.com");
		
		BDDMockito.when(userService.insert(Mockito.any())).thenReturn(savedUser);
		
		String json = new ObjectMapper().writeValueAsString(newUserDto);

		mvc.perform(MockMvcRequestBuilders.post(USER_URI)
				.content(json)
				.contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string("Location" , containsString("/users/" + savedUser.getId())));
			
	}

	@Test
	@DisplayName("Deve deletar um usuario")
	public void deleteUserTest() throws Exception {
		BDDMockito.given(userService.findById(Mockito.anyString())).willReturn(User.builder().id("abcde").build());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(USER_URI + "/" + 1L)
				.accept(MediaType.APPLICATION_JSON);

		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNoContent());

	}

	@Test
	@DisplayName("Deve lançar ObjectNotFoundException ao procurar um usuario não cadastrado")
	public void findInvalidUserTest() throws Exception {
		String id = "aaa";

		BDDMockito.given(userService.findById(Mockito.anyString()))
				.willThrow(new ObjectNotFoundException("Object Not Found"));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(USER_URI + "/" + id)
				.accept(MediaType.APPLICATION_JSON);

		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound());

	}
	
	@Test
	@DisplayName("Deve atualizar um usuario")
	public void updateUserTest() throws Exception {
		String id = "abc";
		
		NewUserDTO updateUserDto = new NewUserDTO();
		updateUserDto.setName("Bruno");
		updateUserDto.setEmail("bruno@hotmail.com");
		
		User updatedUser = new User();
		updatedUser.setId(id);
		updatedUser.setName("Bruno");
		updatedUser.setEmail("bruno@hotmail.com");
		
		BDDMockito.when(userService.fromDto(Mockito.any())).thenReturn(updatedUser);
		BDDMockito.when(userService.update(Mockito.any())).thenReturn(updatedUser);
		
		String json = new ObjectMapper().writeValueAsString(updateUserDto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(USER_URI + "/" + id)
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(json);
		
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isNoContent());
		
	}
	
	@Test
	@DisplayName("Dado um id de um usuario deve retornar uma lista de posts")
	public void findPostsTest() throws Exception {
		String id = "abc";
		
		User user = User.builder().id(id).name("bruno").email("bruno@gmail.com").build();
		AuthorDTO author = new AuthorDTO(user);
		
		Post post = Post.builder().id(id).date(new Date()).title("Bom dia").body("hehe").author(author).build();
		post.getComments().add(new CommentDTO("oi", new Date(), author));
		user.getPosts().add(post);
		
		BDDMockito.when(userService.findById(Mockito.anyString())).thenReturn(user);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(USER_URI + "/" + id + "/posts")
				.accept(MediaType.APPLICATION_JSON);
		
		mvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("$[0]").isNotEmpty());
	}
}
