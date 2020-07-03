package com.bruno.dto;

import java.io.Serializable;

import com.bruno.domain.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewUserDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private String email;

	public NewUserDTO(User obj) {
		this.id = obj.getId();
		this.name = obj.getName();
		this.email = obj.getEmail();
	}

}
