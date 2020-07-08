package com.bruno.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bruno.api.repositories.UserRepository;
import com.bruno.api.services.exceptions.ObjectNotFoundException;
import com.bruno.domain.model.User;
import com.bruno.dto.NewUserDTO;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

	@MockBean
	UserRepository userRepository;

	@InjectMocks
	UserService userService;

	@BeforeEach
	public void setUp() {
		this.userService = new UserService(userRepository);
	}

	@Test
	@DisplayName("Dado um id valido, deve retornar um usuario")
	public void findByIdTest() {
		String id = "abc";
		User user = User.builder().id(id).name("Bruno").email("bruno@gmail.com").build();

		Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(user));

		// exec
		User foundUser = userService.findById(id);

		// verific
		Assertions.assertThat(foundUser).isNotNull();
		Assertions.assertThat(foundUser.getId()).isEqualTo(id);
		Assertions.assertThat(foundUser.getName()).isEqualTo(user.getName());
		Assertions.assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());

	}

	@Test
	@DisplayName("Deve lançar a exception objectNotFound quando não encontrar um usuario")
	public void findByIdInvalidTest() {
		// cenario
		String id = "";

		Mockito.when(userRepository.findById(id)).thenThrow(ObjectNotFoundException.class);

		// exec
		Throwable exception = Assertions.catchThrowable(() -> userService.findById(id));

		// verific
		Assertions.assertThat(exception).isInstanceOf(ObjectNotFoundException.class);
	}

	@Test
	@DisplayName("Deve retornar uma lista de usuarios")
	public void findAllTest() {
		// cenario
		User user = User.builder().id("abc").name("Bruno").email("bruno@gmail.com").build();
		List<User> usersList = new ArrayList<>();
		usersList.add(user);
		Mockito.when(userRepository.findAll()).thenReturn(usersList);

		// exec
		List<User> foundList = userService.findAll();

		// verific
		Assertions.assertThat(foundList.size()).isEqualTo(1);
		Assertions.assertThat(foundList).isEqualTo(usersList);
	}

	@Test
	@DisplayName("Deve inserir um usuario")
	public void insertTest() {
		// cenario
		User user = User.builder().name("Bruno").email("bruno@gmail.com").build();

		Mockito.when(userRepository.save(user))
				.thenReturn(User.builder().id("abc").name("Bruno").email("bruno@gmail.com").build());

		// exec
		User savedUser = userService.insert(user);

		Assertions.assertThat(savedUser.getId()).isNotNull();
		Assertions.assertThat(savedUser.getName()).isEqualTo(user.getName());
		Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());

	}

	@Test
	@DisplayName("Deve atualizar um usuario")
	public void updateTest() {
		User updatingUser = User.builder().id("abc").name("Bruno").email("bruno@gmail.com").build();
		User updatedUser = User.builder().id("abc").name("Bruno").email("bruno@gmail.com").build();

		Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(updatingUser));
		Mockito.when(userRepository.save(updatingUser)).thenReturn(updatedUser);

		User savedUser = userService.update(updatingUser);

		Assertions.assertThat(savedUser.getId()).isNotNull();
		Assertions.assertThat(savedUser.getName()).isEqualTo(updatingUser.getName());
		Assertions.assertThat(savedUser.getEmail()).isEqualTo(updatingUser.getEmail());

	}

	@Test
	@DisplayName("Deve deletar um usuario")
	public void deleteByIdTest() {
		String id = "abc";
		User user = User.builder().id(id).build();

		Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

		// exec
		org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> userService.deleteById(id));

		// verific
		Mockito.verify(userRepository, Mockito.times(1)).deleteById(id);

	}

	@Test
	@DisplayName("Deve lançar ObjectNotFound ao tentar deletar um livro inexistente")
	public void deleteByIdInvalidTest() {
		String id = "abc";

		// Mockito.when(userRepository.findById(id)).thenThrow(ObjectNotFoundException.class);

		org.junit.jupiter.api.Assertions.assertThrows(ObjectNotFoundException.class, () -> userService.deleteById(id));

		Mockito.verify(userRepository, Mockito.times(0)).deleteById(id);
	}

	@Test
	@DisplayName("Deve passar um usuarioDto para um usuario")
	public void fromDtoTest() {
		NewUserDTO userDto = new NewUserDTO();
		userDto.setName("bruno");
		userDto.setEmail("bruno@gmail.com");

		User user = userService.fromDto(userDto);

		Assertions.assertThat(user.getId()).isNull();
		Assertions.assertThat(user.getName()).isEqualTo(userDto.getName());
		Assertions.assertThat(user.getEmail()).isEqualTo(userDto.getEmail());

	}
}
