package com.board.qfit.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.qfit.dto.MemberDTO;
import com.board.qfit.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    //로그인 화면
    @GetMapping("/login")
    public String loginForm() {
        return "member/login";
    }

    //로그인 버튼
//    @PostMapping("/login")
//    public String login(@RequestParam String memberId, @RequestParam String password, Model model) {
//        boolean success = memberService.login(memberId, password);
//        if (success) {
//            return "redirect:/board/";
//        } else {
//            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다. 다시 시도해주세요.");
//            return "member/login";
//        }
//    }
    
    @PostMapping("/login")
    public String login(@RequestParam String memberId, 
    					@RequestParam String password,
    					HttpServletRequest request,
    					Model model) {
    	
        MemberDTO member = memberService.findById(memberId);
        
        if (member != null && member.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("memberId", memberId);
            session.setAttribute("username", member.getName()); //댓글 작성자란에 출력할 유저 이름
            session.setAttribute("role", member.getRole()); // 권한 정보를 세션에 저장
            
            System.out.println("로그인 성공: " + memberId + ", 권한: " + member.getRole());
            return "redirect:/board/";
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다. 다시 시도해주세요.");
            return "member/login";
        }
    }
    
    //회원가입 폼
    @GetMapping("/join")
    public String joinForm() {
        return "member/joinForm";
    }

    //회원가입 버튼
    @PostMapping("/join")
    @ResponseBody
    public Map<String, Object> join(@RequestParam String memberId,
                             @RequestParam String name,
                             @RequestParam String nickname,
                             @RequestParam String password,
                             @RequestParam String confirmPassword
) {
    	
    	Map<String, Object> response = new HashMap<String, Object>();
    	
        if (!password.equals(confirmPassword)) {
        	response.put("success", false);
        	response.put("error", "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
        	return response;
        }

        if (memberService.isMemberIdExist(memberId)) {
        	response.put("success", false);
        	response.put("error", "중복된 아이디입니다. 다른 아이디로 입력해주세요!");
        	return response;
        }
        
        MemberDTO memberDTO = new MemberDTO();
        //입력받은 내용으로 변경
        memberDTO.setMemberId(memberId);
        memberDTO.setName(name);
        memberDTO.setNickname(nickname);
        memberDTO.setPassword(password);

        //회원가입하고나서 success 화면으로 전달해서 회원가입 완료!
        memberService.memberSave(memberDTO);
        response.put("success", true);
        
        return response;
    }
    
    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/member/login";
    }
    
    //id 중복 체크 ajax 방식 이용
    @GetMapping("/checkId")
    @ResponseBody
    public boolean checkId(@RequestParam String memberId) {
        return memberService.isMemberIdExist(memberId);
    }
    
}
