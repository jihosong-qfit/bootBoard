package com.board.qfit.repository;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.board.qfit.dto.MemberDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor //초기화되지 않은 final 필드 생성
public class MemberRepository {
	
    private final SqlSessionTemplate sql;

    public MemberDTO findById(String memberId) {
        return sql.selectOne("Member.findById", memberId);
    }
    public void memberSave(MemberDTO memberDTO) {
    	sql.insert("Member.memberSave", memberDTO);
    }

	public List<MemberDTO> findAll() {
		return sql.selectList("Member.findAll");
	}

	public List<MemberDTO> searchName(String name) {
		return sql.selectList("Member.searchName", name);
	}
	public void deleteMember(String memberId) {
		sql.delete("Member.deleteMember", memberId);
	}
}
