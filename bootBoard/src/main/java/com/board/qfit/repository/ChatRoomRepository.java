package com.board.qfit.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.board.qfit.dto.BoardDTO;
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
	public ChatRoomDTO chatRoom(Long id) {
        ChatRoomDTO chatRoom = sql.selectOne("Chat.chatRoom", id);
        if (chatRoom != null) {
        	//채팅방 접속자 목록 조회
            List<MemberDTO> members = sql.selectList("Chat.findMembersByChatRoomId", id);
            chatRoom.setMembers(members);
        }
        return chatRoom;
    }
	
	// 채팅방에 속한 멤버 목록 조회
    public List<MemberDTO> findMembersByChatRoomId(Long chatRoomId) {
        return sql.selectList("Chat.findMembersByChatRoomId", chatRoomId);
    }
	
    //접속자 수 증가
    public void incrementConnectedUsers(Long chatRoomId) {
        sql.update("Chat.incrementConnectedUsers", chatRoomId);
    }

    //접속자 수 감소
    public void decrementConnectedUsers(Long chatRoomId) {
        sql.update("Chat.decrementConnectedUsers", chatRoomId);
    }

    //채팅방 접속자수 최대 인원
    public int getConnectedUsers(Long chatRoomId) {
        Integer connectedUsers = sql.selectOne("Chat.getConnectedUsers", chatRoomId);
    	return connectedUsers != null ? connectedUsers : 0; //null인 경우 기본값 0 반환
    }
    
    //접속자 최대인원 가져오기
    public int getUserLimit(Long chatRoomId) {
        Integer userLimit = sql.selectOne("Chat.getUserLimit", chatRoomId);
    	return userLimit != null ? userLimit : 0; //null인 경우 기본값 0 반환
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
	public void kickUser(String userId, Long chatRoomId) {
		sql.delete("Chat.kickUser", Map.of("chatRoomId", chatRoomId, "userId", userId));
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

	//사용자가 채팅방에 있는지 확인, DB에 중복 추가 불가
	public boolean checkUserExists(Long chatRoomId, String userId) {
		Integer count = sql.selectOne("Chat.checkUserExists", Map.of("chatRoomId", chatRoomId, "userId", userId));
        return count != null && count > 0;
	}
	
	//채팅방 입장 시 유저 정보 추가
	public void addUserToRoom(Long chatRoomId, String userId) {
		//이미 채팅방에 있는 사용자는 유저 정보 또 추가하지 않도록 사용자의 채팅방 정보가 테이블에 없을 때만 추가해준다.
		if (!checkUserExists(chatRoomId, userId)) {
			Map<String, Object> params = new HashMap<>();
	        params.put("chatRoomId", chatRoomId);
	        params.put("userId", userId);
	        sql.insert("Chat.addUserToRoom", params);	
		}
	}

	//채팅방 나가기 시 메서드
	public void removeUserFromRoom(Long chatRoomId, String userId) {
		 Map<String, Object> params = new HashMap<>();
	        params.put("chatRoomId", chatRoomId);
	        params.put("userId", userId);
	        sql.delete("Chat.removeUserFromRoom", params);
	}
	
	// 채팅방의 현재 접속자 수를 반환하는 메서드
    public int countUsersInRoom(Long chatRoomId) {
        return sql.selectOne("Chat.countUsersInRoom", chatRoomId);
    }
    
    //이전 채팅방 검색 메서드
    public List<ChatRoomDTO> searchByMessageSender(Map<String, Object> searchInfo) {
		return sql.selectList("Chat.searchByMessageSender", searchInfo);
	}

    //이전 채팅방 정보 조회 메서드
	public List<ChatRoomDTO> chatMessageList(Map<String, Object> pageInfo) {
		return sql.selectList("Chat.chatMessageList", pageInfo);
	}
	
	//접속자 수 0으로 설정
	public void setConnectedUsersToZero(Long chatRoomId) {
        sql.update("Chat.setConnectedUsersZero", chatRoomId);
    }

	//채팅방 삭제
    public void deleteChatRoom(Long chatRoomId) {
        sql.delete("Chat.deleteChatRoom", chatRoomId);
    }

    //채팅방 방장 가져오기
    public String getChatRoomHost(Long chatRoomId) {
        return sql.selectOne("Chat.getChatRoomHost", chatRoomId);
    }
    
}
