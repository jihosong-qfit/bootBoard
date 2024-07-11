package com.board.qfit.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.board.qfit.dto.ChatRoomDTO;
import com.board.qfit.dto.ChatRoomForm;
import com.board.qfit.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatRoomService chatRoomService;
	
		//채팅방 생성 화면
	 	@GetMapping("/create")
	    public String createChatRoomForm(Model model) {
	        model.addAttribute("chatRoomForm", new ChatRoomForm());
	        return "chat/chatCreate";
	    }

	 	//채팅방 생성
	    @PostMapping("/create")
	    public String createChatRoom(@ModelAttribute ChatRoomForm chatRoomForm) {
	    	//사용자에게 입력받은 채팅방정보를 바탕으로 채팅방 생성
	        chatRoomService.chatCreate(chatRoomForm);
	        return "redirect:/chat/chatList";
	    }

	    //채팅방 목록 화면
	    @GetMapping("/chatList")
	    public String listChatRooms(Model model) {
	        List<ChatRoomDTO> chatRooms = chatRoomService.chatList();
	        model.addAttribute("chatRooms", chatRooms);
	        return "chat/chatList";
	    }

	    //채팅방 상세 조회 화면
	    @GetMapping("/chatRoom/{id}")
	    public String enterChatRoom(@PathVariable Long id, Model model) {
	        ChatRoomDTO chatRoom = chatRoomService.chatRoom(id);
	        model.addAttribute("chatRoom", chatRoom);
	        return "chat/chatRoom";
	    }
	
}
