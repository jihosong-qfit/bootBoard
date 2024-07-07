package com.board.qfit.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.board.qfit.dto.BoardDTO;

@Repository
public class BoardRepository {
	
	private final SqlSessionTemplate sql;
	
	public BoardRepository(SqlSessionTemplate sql) {
		this.sql = sql;
	}
	
	//게시글 등록
	public int save(BoardDTO boardDTO) {
		return sql.insert("Board.save", boardDTO); //<mapper namespace="Board">
	}

	//게시글 목록
	public List<BoardDTO> findAll() {
		return sql.selectList("Board.findAll");
	}

	public BoardDTO findById(Long boardno) {
		return sql.selectOne("Board.findById", boardno);
	}

	public void updateHits(Long boardno) {
		sql.update("Board.updateHits", boardno);
	}

	public void delete(Long boardno) {
		sql.delete("Board.delete", boardno);
	}

	public void update(BoardDTO boardDTO) {
		sql.update("Board.update", boardDTO);
	}

	public List<BoardDTO> findAllPaging(Map<String, Object> pageInfo) {
		return sql.selectList("Board.findAllPaging", pageInfo);
	}

	public int countAll() {
		return sql.selectOne("Board.countAll");
	}

	public List<BoardDTO> searchTitle(String title) {
		return sql.selectList("Board.searchTitle", title);
	}

}
