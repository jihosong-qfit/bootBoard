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
	
	public void addUserToChatRoom(String userId, String username) {
        chatRoomRepository.addUserToChatRoom(userId, username);
    }
	
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

	
}
