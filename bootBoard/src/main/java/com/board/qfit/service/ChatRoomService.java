package com.board.qfit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.qfit.dto.ChatRoomDTO;
import com.board.qfit.dto.ChatRoomForm;
import com.board.qfit.dto.ChatUser;
import com.board.qfit.dto.MemberDTO;
import com.board.qfit.repository.ChatRoomRepository;
import com.board.qfit.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final MemberRepository memberRepository;
	
	/*사용자가 입력한 채팅방이름, 비밀번호, 인원제한수, 방장정보를 바탕으로 채팅방 생성*/
	public void chatCreate(ChatRoomForm chatRoomForm) {
		ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
		chatRoomDTO.setName(chatRoomForm.getName());
		chatRoomDTO.setPassword(chatRoomForm.getPassword());
		chatRoomDTO.setLimit(chatRoomForm.getLimit());
		chatRoomDTO.setHost(chatRoomForm.getHost());
		
		//채팅방 생성
		chatRoomRepository.chatCreate(chatRoomDTO);
		
		//생성된 채팅방 id 가져오기
		Long chatRoomId = chatRoomDTO.getId();
		
		//방장이 memberid를 가지고 있으므로 chat_room_members 테이블에 추가
//		chatRoomRepository.addUserToChatRoom(chatRoomId, chatRoomForm.getHost());
	}
	
	//채팅방 목록 조회
	public List<ChatRoomDTO> chatList() {
		return chatRoomRepository.chatList();
	}
	
	//채팅방 입장 시 유저id, 유저이름 정보 테이블에 추가
//	public void addUserToChatRoom(String userId, String username) {
//        chatRoomRepository.addUserToChatRoom(userId, username);
//    }
	
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

    public int getConnectedUsers(Long chatRoomId) {
        return chatRoomRepository.getConnectedUsers(chatRoomId);
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
    public void kickUser(String userId) {
        chatRoomRepository.kickUser(userId);
    }
    
    //채팅내용 갱신
    public List<ChatRoomDTO> getMessages(Long chatRoomId) {
        List<ChatRoomDTO> messages = chatRoomRepository.findMessagesByChatRoomId(chatRoomId);
        messages.forEach(message -> {
        	//귓속말은 특정 대상 수신인에게만 보내는 것이므로 recipient가 있고 null이 아닐 때, 있을 때
            boolean whisper = message.getRecipient() != null;
            //귓속말 설정으로 변경
            message.setWhisper(whisper);
        });
        return messages; //귓속말 생성자에 해당하는 것으로 메시지 전송
    }
    
    //채팅방 목록 조회
  	public List<ChatUser> getChatUser() {
  		return chatRoomRepository.getChatUser();
  	}

  	//채팅방 입장시 비밀번호 유효성 검증
	public boolean validatePassword(Long roomId, String password) {
		ChatRoomDTO chatRoom = chatRoomRepository.chatRoom(roomId);
        if (chatRoom != null) {
            return chatRoom.getPassword().equals(password);
        }
        return false;
	}

	//사용자 채팅방 입장
	public boolean enterRoom(Long chatRoomId, String userId) {
		ChatRoomDTO room = chatRoomRepository.chatRoom(chatRoomId);
		
		// 이미 접속 중인 경우 접속자 수의 증가 없이 입장처리만
        if (checkUserExists(chatRoomId, userId)) {
            return true;
        }
		
        if (room.getConnectedUsers() < room.getLimit()) { //최대 인원수를 초과하지 않을 때만
            chatRoomRepository.incrementConnectedUsers(chatRoomId); //입장 시 접속자수 증가
            chatRoomRepository.addUserToRoom(chatRoomId, userId); //입장 시 유저정보 인서트
            return true;
        }
        return false;
	}

	//사용자 채팅방 나가기
	public void leaveRoom(Long chatRoomId, String userId) {
        chatRoomRepository.decrementConnectedUsers(chatRoomId); //나가기 시 접속자수 줄어듬
        chatRoomRepository.removeUserFromRoom(chatRoomId, userId); //나가기 시 유저정보 삭제
    }
	
	// 사용자가 채팅방에 있는지 확인하는 메서드
    public boolean checkUserExists(Long chatRoomId, String userId) {
        return chatRoomRepository.checkUserExists(chatRoomId, userId);
    }
	
}
