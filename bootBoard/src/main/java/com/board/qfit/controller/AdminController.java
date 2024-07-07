package com.board.qfit.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.qfit.dto.MemberDTO;
import com.board.qfit.service.MemberService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final MemberService memberService;
	
	public AdminController(MemberService memberService) {
        this.memberService = memberService;
    }
	
	//관리자 페이지 화면
	@GetMapping("/adminPage")
    public String adminPage(Model model) {
        List<MemberDTO> memberList = memberService.findAll();
        model.addAttribute("memberList", memberList);
        return "admin/adminPage";
    }
	
	//관리자 페이지 이름으로 검색
	@GetMapping("/search")
    public String search(@RequestParam("name") String name, Model model) {
        List<MemberDTO> memberList = memberService.searchName(name);
        model.addAttribute("memberList", memberList);
        return "admin/adminPage";
    }
}
