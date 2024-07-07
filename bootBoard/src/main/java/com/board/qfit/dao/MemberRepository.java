package com.board.qfit.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.board.qfit.dto.MemberDTO;

@Repository
public class MemberRepository {
    private final SqlSessionTemplate sql;

    public MemberRepository(SqlSessionTemplate sql) {
        this.sql = sql;
    }

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
}
