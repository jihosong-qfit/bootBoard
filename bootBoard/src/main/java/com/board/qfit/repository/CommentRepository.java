package com.board.qfit.repository;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.board.qfit.dto.CommentDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

	private final SqlSessionTemplate sql;
	
	//댓글 작성
	public void addComment(CommentDTO comment) {
		sql.insert("Comment.addComment", comment);
    }
	
	//댓글 조회
	public List<CommentDTO> showComment(Long boardNo) {
		return sql.selectList("Comment.showComment", boardNo);
	}
}
