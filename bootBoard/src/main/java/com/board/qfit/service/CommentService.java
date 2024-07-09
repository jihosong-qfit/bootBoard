package com.board.qfit.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.board.qfit.dto.CommentDTO;
import com.board.qfit.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

private final CommentRepository commentRepository;
	
	//댓글 작성
	public void addComment(CommentDTO comment) {
		commentRepository.addComment(comment);
	}
	
	//댓글 조회
	public List<CommentDTO> showComment(Long boardNo) {
		return commentRepository.showComment(boardNo);
	}
	
}
