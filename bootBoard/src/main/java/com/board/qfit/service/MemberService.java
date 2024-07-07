package com.board.qfit.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.qfit.dao.MemberRepository;
import com.board.qfit.dto.MemberDTO;

@Service
public class MemberService {
	
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberDTO findById(String memberId) {
        return memberRepository.findById(memberId);
    }
    
    //회원가입
    //중복된 id가 있을 경우 트랜잭션을 롤백하고 메시지를 반환한다.
    @Transactional
    public void memberSave(MemberDTO memberDTO) {
        memberRepository.memberSave(memberDTO);
    }
    
    //로그인 로직
    public boolean login(String memberId, String password) {
    	//db의 memberId null 체크, 패스워드가 같아야 함
        MemberDTO member = memberRepository.findById(memberId);
        return member != null && member.getPassword().equals(password);
    }

    //관리자 페이지 리스트
	public List<MemberDTO> findAll() {
		return memberRepository.findAll();
	}
	
	//관리자 페이지 이름 검색
	public List<MemberDTO> searchName(String name) {
        return memberRepository.searchName(name);
    }
	
	public boolean isMemberIdExist(String memberId) {
        return memberRepository.findById(memberId) != null;
    }
	
}
