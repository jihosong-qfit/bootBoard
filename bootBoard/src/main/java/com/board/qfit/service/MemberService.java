package com.board.qfit.service;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.qfit.dto.MemberDTO;
import com.board.qfit.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
    private final MemberRepository memberRepository;

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
	
	//회원 id 확인
	public boolean isMemberIdExist(String memberId) {
        return memberRepository.findById(memberId) != null;
    }

	//회원 삭제 => adminController
	public void deleteMember(String memberId) {
		memberRepository.deleteMember(memberId);
	}
	
	//관리자인지 확인
	public boolean isAdmin(String memberId) {
		MemberDTO member = memberRepository.findById(memberId);
		return member.getRole().equals("ROLE_ADMIN");
	}
	
}
