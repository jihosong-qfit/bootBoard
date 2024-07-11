package com.board.qfit.dto;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDTO {
	
	private Long boardno;
	private String title;
	private String writer;
	private String content;
	private String createdate;
	private int hit;
	private String memberId;
	
}
