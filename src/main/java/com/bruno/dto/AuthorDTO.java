package com.bruno.dto;

import java.io.Serializable;

import com.bruno.domain.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;

	public AuthorDTO(User user) {
		this.id = user.getId();
		this.name = user.getName();
	}
}
