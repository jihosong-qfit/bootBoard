package com.board.qfit.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.qfit.dto.BoardDTO;
import com.board.qfit.service.BoardService;


@Controller
@RequestMapping("/board")
public class BoardController {
	
	private final BoardService boardService;
	
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	//게시글 작성 화면
	@GetMapping("/save")
	public String save() {
		return "board/save";
	}
	
	//게시글 작성 버튼
	@PostMapping("/save")
	public String save(@ModelAttribute BoardDTO boardDTO) {
		int saveResult = boardService.save(boardDTO);
		if (saveResult > 0) {
			return "redirect:/board/";
		} else {
			return "board/save";
		}
	}
	
	//게시글 목록
//	@GetMapping("/")
//	public String findAll(Model model) {
//		List<BoardDTO> boardDTOList = boardService.findAll();
//		model.addAttribute("boardList", boardDTOList);
//		return "board/list";
//	}
	
	//페이징 처리
	@GetMapping("/")
    public String findAll(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Model model) {
        List<BoardDTO> boardDTOList = boardService.findAllPaging(page, size);
        int totalRecords = boardService.countAll();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        model.addAttribute("boardList", boardDTOList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "board/list";
    }
	
	//게시글 상세
	@GetMapping
	public String findById(@RequestParam("boardno") Long boardno, Model model) {
		//1. 조회수가 올라가고 나서
		boardService.updateHits(boardno);
		//2. 게시물의 정보를 가져온다.
		BoardDTO boardDTO = boardService.findById(boardno);
		model.addAttribute("board", boardDTO);
		return "board/detail";
	}
	
	//게시글 삭제
	@GetMapping("/delete")
	public String delete(@RequestParam("boardno") Long boardno) {
		boardService.delete(boardno);
		return "redirect:/board/";
	}
	
	//게시글 수정 화면
	@GetMapping("/update")
	public String updateForm(@RequestParam("boardno") Long boardNo, Model model) {
		BoardDTO boardDTO = boardService.findById(boardNo);
		model.addAttribute("board", boardDTO);
		return "/board/update";
	}
	
	//게시글 수정
	@PostMapping("/update")
	public String update(@ModelAttribute BoardDTO boardDTO, Model model) {
		boardService.update(boardDTO);
		BoardDTO dto =  boardService.findById(boardDTO.getBoardno());
		model.addAttribute("board", dto);
		return "/board/detail";
	}
	
	//검색 기능
	@GetMapping("/search")
    public String search(@RequestParam("title") String title, Model model) {
        List<BoardDTO> boardDTOList = boardService.searchTitle(title);
        model.addAttribute("boardList", boardDTOList);
        return "board/list";
    }
	
	
}
