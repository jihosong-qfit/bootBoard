package com.board.qfit.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.qfit.dto.ChatRoomDTO;
import com.board.qfit.dto.ChatRoomForm;
import com.board.qfit.dto.MemberDTO;
import com.board.qfit.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	
	/*사용자가 입력한 채팅방이름, 비밀번호, 인원제한수, 방장정보를 바탕으로 채팅방 생성*/
	public void chatCreate(ChatRoomForm chatRoomForm) {
		ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
		chatRoomDTO.setName(chatRoomForm.getName());
		chatRoomDTO.setPassword(chatRoomForm.getPassword());
		chatRoomDTO.setLimit(chatRoomForm.getLimit());
		chatRoomDTO.setHost(chatRoomForm.getHost());
		chatRoomRepository.chatCreate(chatRoomDTO);
	}
	
	//채팅방 목록 조회
	public List<ChatRoomDTO> chatList() {
		return chatRoomRepository.chatList();
	}
	
//	//입장 시 접속자 추가------
//	public void addUserToRoom(Long chatRoomId, String memberId) {
//        chatRepository.addUserToRoom(chatRoomId, memberId);
//    }
//
//	//나가기 시 접속자 제거
//    public void removeUserFromRoom(Long chatRoomId, String memberId) {
//        chatRepository.removeUserFromRoom(chatRoomId, memberId);
//    }
//
    //접속자 목록 관리
    public List<MemberDTO> getUsersInRoom(Long chatRoomId) {
        return chatRoomRepository.getUsersInRoom(chatRoomId);
    }
    
    //접속자 관리
    public void updateConnectionStatus(Long chatRoomId, String memberId, boolean isConnected) {
    	chatRoomRepository.updateConnectionStatus(memberId, isConnected);
    }
	
	//채팅방 상세 조회
	public ChatRoomDTO chatRoom(Long id) {
		ChatRoomDTO chatRoom = chatRoomRepository.chatRoom(id); //채팅방 상세 조회
		return chatRoom;
	}
	
	//채팅방 입장 접속자수 +1
	@Transactional
	public void upConnectedUsers(Long id) {
		 int result = chatRoomRepository.upConnectedUsers(id);
	        if (result == 0) {
	            throw new IllegalStateException("접속유저수 변경 실패");
	        }
	}
		
	@Transactional
	//채팅방 나가기 접속자수 -1
	public void downConnectedUsers(Long id) {
		int result = chatRoomRepository.downConnectedUsers(id);
        if (result == 0) {
            throw new IllegalStateException("접속유저수 변경 실패");
        }
	}
	
	//채팅내용 전송
	public void sendMessage(Long chatRoomId, String message, String sender) {
		chatRoomRepository.sendMessage(chatRoomId, message, sender);
    }

	//귓속말버튼
    public void sendWhisper(Long chatRoomId, String sender, String recipient, String message) {
    	chatRoomRepository.sendWhisper(chatRoomId, sender, recipient, message);
    }

    //강퇴버튼
    public void kickUser(Long chatRoomId, String username) {
    	chatRoomRepository.kickUser(chatRoomId, username);
    }

    //채팅내용 갱신
    public List<ChatRoomDTO> getMessages(Long chatRoomId) {
        return chatRoomRepository.findMessagesByChatRoomId(chatRoomId);
    }
	
}
