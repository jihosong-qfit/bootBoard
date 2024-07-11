package com.board.qfit.repository;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.board.qfit.dto.ChatRoomDTO;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

	private final SqlSessionTemplate sql;
	
	//채팅방 생성
	public void chatCreate(ChatRoomDTO chatRoomDTO) {
		sql.insert("Chat.chatCreate", chatRoomDTO);
	}
	
	//채팅방 목록 조회
	public List<ChatRoomDTO> chatList() {
		return sql.selectList("Chat.chatList");
	}
	
	//채팅방 상세 조회
	public ChatRoomDTO chatRoom(Long id) {
		return sql.selectOne("Chat.chatRoom", id);
	}

}
