package com.board.qfit.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDTO {

	private Long id;
	private Long boardno;
	private String writer;
	private String content;
	private Timestamp createdate;
	
}
