package com.board.qfit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.board.qfit.dto.CommentDTO;
import com.board.qfit.service.CommentService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/comment")
public class CommentController {
	
	private final CommentService commentService;
	
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	//댓글 작성
	@PostMapping("/addComment")
	public String addComment(CommentDTO comment, HttpSession session) {
		String username = (String) session.getAttribute("username");
		comment.setWriter(username);
		commentService.addComment(comment);
		return "redirect:/board?boardno=" + comment.getBoardno();
	}
	
}
