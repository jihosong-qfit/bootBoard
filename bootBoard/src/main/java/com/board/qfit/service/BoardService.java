package com.board.qfit.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.board.qfit.dto.BoardDTO;
import com.board.qfit.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;

	//게시글 등록
	public int save(BoardDTO boardDTO) {
		return boardRepository.save(boardDTO);
	}
	
	//게시글 작성 시 로그인 유저 정보 memberId 가져오기
	public BoardDTO saveMemberId(Long boardno, String memberId) {
		return boardRepository.saveMemberId(boardno, memberId);
	}

	//게시글 목록
	public List<BoardDTO> findAll() {
		return boardRepository.findAll();
	}

	//게시글 상세
	public BoardDTO findById(Long boardno) {
		return boardRepository.findById(boardno);
	}

	//게시글 조회수 올리기
	public void updateHits(Long boardno) {
		boardRepository.updateHits(boardno);
	}

	//게시글 삭제
	public void delete(Long boardno) {
		boardRepository.delete(boardno);
	}

	//게시글 수정
	public void update(BoardDTO boardDTO) {
		boardRepository.update(boardDTO);
	}
	
	//페이징 처리
	 public List<BoardDTO> findAllPaging(int page, int size) {
	        int offset = (page - 1) * size;
	        Map<String, Object> pageInfo = new HashMap<>();
	        pageInfo.put("limit", size);
	        pageInfo.put("offset", offset);
	        return boardRepository.findAllPaging(pageInfo);
	}

	 //게시글 작성자만 상세 내용 조회
	 public boolean isBoardWriter(Long boardno, String memberId) {
	        return boardRepository.isBoardWriter(boardno, memberId);
	    }
	 
	 //게시글 전체 개수
	public int countAll() {
		return boardRepository.countAll();
	}
	
	//검색 기능
//	public List<BoardDTO> searchTitle(String title) {
//        return boardRepository.searchTitle(title);
//    }

	 public List<BoardDTO> searchByTitleWriter(int page, int size, String searchType, String keyword) {
	        int offset = (page - 1) * size;
	        Map<String, Object> searchInfo = new HashMap<>();
	        searchInfo.put("limit", size);
	        searchInfo.put("offset", offset);
	        searchInfo.put("searchType", searchType);
	        searchInfo.put("keyword", keyword);
	        return boardRepository.searchByTitleWriter(searchInfo);
	    }
	
}
