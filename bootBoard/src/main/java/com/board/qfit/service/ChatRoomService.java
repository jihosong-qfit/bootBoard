package com.board.qfit.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.board.qfit.dto.ChatRoomDTO;
import com.board.qfit.dto.ChatRoomForm;
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
	
	//채팅방 상세 조회
	public ChatRoomDTO chatRoom(Long id) {
		return chatRoomRepository.chatRoom(id);
	}
	
}
