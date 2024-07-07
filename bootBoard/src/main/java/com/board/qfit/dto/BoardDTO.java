package com.board.qfit.dto;

import java.sql.Timestamp;

public class BoardDTO {
	
	private Long boardno;
	private String title;
	private String writer;
	private String content;
	private String createdate;
	private int hit;
	
	public Long getBoardno() {
		return boardno;
	}
	public void setBoardno(Long boardno) {
		this.boardno = boardno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	@Override
	public String toString() {
		return "BoardDTO [boardno=" + boardno + ", title=" + title + ", writer=" + writer + ", content=" + content
				+ ", createdate=" + createdate + ", hit=" + hit + "]";
	}
	
}
