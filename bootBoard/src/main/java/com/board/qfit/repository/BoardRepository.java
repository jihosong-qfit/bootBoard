package com.board.qfit.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.board.qfit.dto.BoardDTO;

import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class BoardRepository {
	
	private final SqlSessionTemplate sql;
	
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

	//게시글 목록 조회 + 페이징 처리
	public List<BoardDTO> findAllPaging(Map<String, Object> pageInfo) {
		return sql.selectList("Board.findAllPaging", pageInfo);
	}

	public int countAll() {
		return sql.selectOne("Board.countAll");
	}

	public List<BoardDTO> searchTitle(String title) {
		return sql.selectList("Board.searchTitle", title);
	}

	//게시글 작성 시 로그인 유저 정보 memberId 가져오기
	public BoardDTO saveMemberId(Long boardno, String memberId) {
		Map<String, Object> memberInfo = new HashMap<String, Object>();
		memberInfo.put("boardno", boardno);
		memberInfo.put("memberId", memberId);
		return sql.selectOne("Board.saveMemberId", memberInfo);
	}

	//게시글 작성한 유저만 상세
	public boolean isBoardWriter(Long boardno, String memberId) {
        int count = sql.selectOne("Board.isBoardWriter", Map.of("boardno", boardno, "memberId", memberId));
        return count > 0;
    }

	public List<BoardDTO> searchByTitleWriter(Map<String, Object> searchInfo) {
		return sql.selectList("Board.searchByTitleWriter", searchInfo);
	}

}
