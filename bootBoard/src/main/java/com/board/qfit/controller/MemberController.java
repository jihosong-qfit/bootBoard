package com.board.qfit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String login(@RequestParam String memberId, @RequestParam String password, HttpServletRequest request, Model model) {
        MemberDTO member = memberService.findById(memberId);
        if (member != null && member.getPassword().equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("memberId", memberId);
            session.setAttribute("role", member.getRole()); // 권한 정보를 세션에 저장
            // 디버그 출력
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
    public String join(@RequestParam String memberId,
                             @RequestParam String name,
                             @RequestParam String nickname,
                             @RequestParam String password,
                             @RequestParam String confirmPassword,
                             Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.\n다시 입력해주세요.");
            return "member/joinForm";
        }

        if (memberService.isMemberIdExist(memberId)) {
            model.addAttribute("error", "중복된 아이디입니다.\n다른 아이디로 입력해주세요!");
            return "member/joinForm";
        }
        
        MemberDTO memberDTO = new MemberDTO();
        //입력받은 내용으로 변경
        memberDTO.setMemberId(memberId);
        memberDTO.setName(name);
        memberDTO.setNickname(nickname);
        memberDTO.setPassword(password);

        memberService.memberSave(memberDTO);
        return "redirect:/member/login";
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
