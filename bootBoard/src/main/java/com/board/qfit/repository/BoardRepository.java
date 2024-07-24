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
		return sql.insert("Board.save", boardDTO);
	}

	//게시글 목록
	public List<BoardDTO> findAll() {
		return sql.selectList("Board.findAll");
	}

	//게시글 상세
	public BoardDTO findById(Long boardno) {
		return sql.selectOne("Board.findById", boardno);
	}

	//조회수 업데이트
	public void updateHits(Long boardno) {
		sql.update("Board.updateHits", boardno);
	}

	//게시글 삭제
	public void delete(Long boardno) {
		sql.delete("Board.delete", boardno);
	}

	//게시글 수정
	public void update(BoardDTO boardDTO) {
		sql.update("Board.update", boardDTO);
	}

	//게시글 목록 조회 + 페이징 처리
	public List<BoardDTO> findAllPaging(Map<String, Object> pageInfo) {
		return sql.selectList("Board.findAllPaging", pageInfo);
	}

	//전체 게시글 개수 가져오기
	public int countAll() {
		return sql.selectOne("Board.countAll");
	}

	//제목으로 검색
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

	//제목, 작성자, 내용으로 게시글 검색
	public List<BoardDTO> searchByTitleWriter(Map<String, Object> searchInfo) {
		return sql.selectList("Board.searchByTitleWriter", searchInfo);
	}

}
