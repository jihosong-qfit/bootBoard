package com.board.qfit.repository;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.board.qfit.dto.ChatRoomDTO;
import com.board.qfit.dto.ChatUser;
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
//	public ChatRoomDTO chatRoom(Long id) {
//		return sql.selectOne("Chat.chatRoom", id);
//	}
	public ChatRoomDTO chatRoom(Long id) {
        ChatRoomDTO chatRoom = sql.selectOne("Chat.chatRoom", id);
        if (chatRoom != null) {
            List<MemberDTO> members = sql.selectList("Chat.findMembersByChatRoomId", id);
            chatRoom.setMembers(members);
        }
        return chatRoom;
    }
	
	// 채팅방에 속한 멤버 목록 조회
    public List<MemberDTO> findMembersByChatRoomId(Long chatRoomId) {
        return sql.selectList("Chat.findMembersByChatRoomId", chatRoomId);
    }
	
	//채팅방 입장 접속자수 +1
	public int upConnectedUsers(Long id) {
		return sql.update("Chat.upConnectedUsers", id);
	}
	
	//채팅방 나가기 접속자수 -1
	public int downConnectedUsers(Long id) {
		return sql.update("Chat.downConnectedUsers", id);
	}

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
	public void kickUser(String userId) {
		sql.delete("Chat.kickUser", userId);
	}

	//채팅방에 멤버 정보 추가, DB에 중복 추가 불가
	public void addUserToChatRoom(String userId, String username) {
        int userExists = sql.selectOne("Chat.checkUserExists", userId);
        if (userExists == 0) {
            sql.insert("Chat.addUserToChatRoom", 
                Map.of("userId", userId, "username", username));
        }
    }
	
	//채팅내용 수신
	public List<ChatRoomDTO> findMessagesByChatRoomId(Long chatRoomId) {
		return sql.selectList("Chat.findMessagesByChatRoomId", chatRoomId);
	}

	//채팅방 정보 상태 변경
	public void updateChatRoom(ChatRoomDTO chatRoom) {
        sql.update("Chat.updateChatRoom", chatRoom);
	}
	
	//채팅방 유저 정보 가져오기
	public List<ChatUser> getChatUser() {
		return sql.selectList("Chat.getChatUser");
	}

}
