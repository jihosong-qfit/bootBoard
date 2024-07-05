package com.board.qfit.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardDTO {
	
	private Long boardno;
	private String title;
	private String content;
	private String createdate;
	private int hit;
	
}
