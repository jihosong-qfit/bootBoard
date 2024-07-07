package com.board.qfit.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.board.qfit.dao.BoardRepository;
import com.board.qfit.dto.BoardDTO;

@Service
public class BoardService {
	
	private final BoardRepository boardRepository;
	
	public BoardService(BoardRepository boardRepository) {
		this.boardRepository = boardRepository;
	}

	//게시글 등록
	public int save(BoardDTO boardDTO) {
		return boardRepository.save(boardDTO);
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

	 //게시글 전체 개수
	public int countAll() {
		return boardRepository.countAll();
	}
	
	//검색 기능
	public List<BoardDTO> searchTitle(String title) {
        return boardRepository.searchTitle(title);
    }
	
}
