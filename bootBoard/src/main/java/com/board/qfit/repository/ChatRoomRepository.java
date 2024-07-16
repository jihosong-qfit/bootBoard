package com.board.qfit.repository;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.board.qfit.dto.ChatRoomDTO;
import com.board.qfit.dto.MemberDTO;

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
	
	//채팅방 입장 접속자수 +1
	public int upConnectedUsers(Long id) {
		return sql.update("Chat.upConnectedUsers", id);
	}
	
	//채팅방 나가기 접속자수 -1
	public int downConnectedUsers(Long id) {
		return sql.update("Chat.downConnectedUsers", id);
	}

//	//입장 시 접속자 추가
//	public void addUserToRoom(Long chatRoomId, String memberId) {
//		return sql.insert("Chat.addUserToRoom")
//	}
//
//	//나가기 시 접속자 제거
//	public void removeUserFromRoom(Long chatRoomId, String memberId) {
//		chatRepository.removeUserFromRoom(chatRoomId, memberId);
//	}

	//접속자 목록 관리
	public List<MemberDTO> getUsersInRoom(Long chatRoomId) {
		return sql.selectList("Chat.getUsersInRoom", chatRoomId);
	}
	
	//접속자 관리
	public void updateConnectionStatus(String memberId, boolean isConnected) {
        Map<String, Object> params = Map.of("memberId", memberId, "isConnected", isConnected ? 1 : 0);
        sql.update("Chat.updateConnectionStatus", params);
    }


	//채팅내용 전송
	public void sendMessage(Long chatRoomId, String message, String sender) {
		sql.insert("Chat.sendMessage", Map.of("chatRoomId", chatRoomId, "message", message, "sender", sender));
	}
	
	//귓속말
	public void sendWhisper(Long chatRoomId, String sender, String recipient, String message) {
		sql.insert("Chat.sendWhisper", Map.of("chatRoomId", chatRoomId, "sender", sender, "recipient", recipient, "message", message));
	}

	//강퇴
	public void kickUser(Long chatRoomId, String username) {
		sql.delete("Chat.kickUser", Map.of("chatRoomId", chatRoomId, "username", username));
	}

	//채팅내용 수신
	public List<ChatRoomDTO> findMessagesByChatRoomId(Long chatRoomId) {
		return sql.selectList("Chat.findMessagesByChatRoomId", chatRoomId);
	}

}
