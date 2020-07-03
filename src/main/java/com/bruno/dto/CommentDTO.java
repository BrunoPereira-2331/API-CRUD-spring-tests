package com.bruno.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String text;
	private Date date;
	private AuthorDTO authorDto;

	public CommentDTO() {
		super();
	}

	public CommentDTO(String text, Date date, AuthorDTO authorDto) {
		super();
		this.text = text;
		this.date = date;
		this.authorDto = authorDto;
	}

}
