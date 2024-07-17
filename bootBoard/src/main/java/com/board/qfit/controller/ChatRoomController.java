package com.board.qfit.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.qfit.dto.ChatRoomDTO;
import com.board.qfit.dto.ChatRoomForm;
import com.board.qfit.dto.MemberDTO;
import com.board.qfit.service.ChatRoomService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatRoomService chatRoomService;
	
		//채팅방 생성 화면
	 	@GetMapping("/chatCreate")
	    public String createChatRoomForm(Model model, HttpSession session) {
	 		//게시판 목록 session에 저장되어있던 로그인 아이디값을 가져온다.
	 		String memberId = (String) session.getAttribute("memberId");
	 		//로그인한 아이디값이 없으면 로그인 페이지로 이동한다.
	 		if (memberId == null) {
	 			return "redirect:/member/login";
	 		}
	 		
	 		ChatRoomForm chatRoomForm = new ChatRoomForm();
	 		chatRoomForm.setHost(memberId); //session에서 가져온 로그인한 사용자 아이디값을 방장으로 설정
	        model.addAttribute("chatRoomForm", chatRoomForm);
	        
	        return "chat/chatCreate";
	    }

	 	//채팅방 생성
	    @PostMapping("/chatCreate")
	    public String createChatRoom(@ModelAttribute ChatRoomForm chatRoomForm, HttpSession session) {
	    	//사용자에게 입력받은 채팅방정보를 바탕으로 채팅방 생성
	    	//게시판 목록 session에 저장되어있던 로그인 아이디값을 가져온다.
	 		String memberId = (String) session.getAttribute("memberId");
	    	if (memberId == null) {
	 			return "redirect:/member/login";
	 		}
	    	
	    	chatRoomForm.setHost(memberId); //session에서 가져온 로그인한 사용자 아이디값을 방장으로 설정
	        chatRoomService.chatCreate(chatRoomForm);
	        return "redirect:/chat/chatList";
	    }

	    //채팅방 목록 화면
	    @GetMapping("/chatList")
	    public String chatListPage(Model model, HttpSession session) {
	    	String memberId = (String) session.getAttribute("memberId");
	        List<ChatRoomDTO> chatRooms = chatRoomService.chatList();
	        model.addAttribute("chatRooms", chatRooms);
	        return "chat/chatList";
	    }

	    //채팅방 상세 조회 화면
	    @GetMapping("/chatRoom/{id}")
	    public String getChatRoom(@PathVariable Long id, Model model, HttpSession session) {
	    	String memberId = (String) session.getAttribute("memberId");
	    	String username = (String) session.getAttribute("username");
	    	//1. 접속자수 관리
	    	chatRoomService.updateConnectionStatus(id, memberId, true);
	    	//2. 채팅방 입장 시 상세 정보
	        ChatRoomDTO chatRoom = chatRoomService.chatRoom(id);
	        model.addAttribute("chatRoom", chatRoom);
	        return "chat/chatRoom";
	    }
	    
	    //채팅방 상세 화면에서 나가기 시 접속자 제거
	    @PostMapping("/removeUser")
	    @ResponseBody
	    public void removeUser(@RequestBody Map<String, Object> payload) {
	        Long chatRoomId = Long.valueOf(payload.get("chatRoomId").toString());
	        String memberId = (String) payload.get("memberId");
	        chatRoomService.updateConnectionStatus(chatRoomId, memberId, false);
	    }

	    //접속자 목록 관리
	    @GetMapping("/getUsersInRoom")
	    @ResponseBody
	    public List<MemberDTO> getUsersInRoom(@RequestParam Long chatRoomId) {
	        return chatRoomService.getUsersInRoom(chatRoomId);
	    }
	    
	    //채팅내용 전송
	    //사용자의 메시지, 채팅방 id를 받아옴
	    @PostMapping("/sendMessage")
	    @ResponseBody
	    public ChatRoomDTO sendMessage(@RequestBody Map<String, Object> payload) {
	    	String message = (String) payload.get("message");
	    	Long chatRoomId = Long.valueOf(payload.get("chatRoomId").toString());
	    	String sender = (String) payload.get("sender");
	    	chatRoomService.sendMessage(chatRoomId, message, sender);
	        return new ChatRoomDTO(chatRoomId, message, sender, null, false);
	    }

	    //귓속말버튼
	    //귓속말 대상자 유저, 채팅내용, id를 받아옴
	    @PostMapping("/sendWhisper")
	    @ResponseBody
	    public ChatRoomDTO sendWhisper(@RequestBody Map<String, Object> payload) {
	    	
	    	String sender = (String) payload.get("sender");
	        String recipient = (String) payload.get("recipient");
	        String message = (String) payload.get("message");
	        Long chatRoomId = Long.valueOf(payload.get("chatRoomId").toString());
	        
	        MemberDTO senderMember = new MemberDTO();
	        senderMember.setMemberId(sender); // 보내는 사람: 유저id
	        
	        MemberDTO recipientMember = new MemberDTO();
	        recipientMember.setName(recipient); // 받는 사람: 유저명
	        
	        chatRoomService.sendWhisper(chatRoomId, sender, recipient, message);
	        ChatRoomDTO response = new ChatRoomDTO(chatRoomId, senderMember.getMemberId(), recipientMember.getName(), message, true);
	        return response;
	    }

	    //강퇴버튼
	    //채팅방 id, 유저이름을 받아옴
//	    @PostMapping("/kickUser")
//	    @ResponseBody
//	    public String kickUser(@RequestParam String username, @RequestParam Long chatRoomId) {
//	    	chatRoomService.kickUser(chatRoomId, username);
//	        return username + "님을 강퇴했습니다.";
//	    }
	    @PostMapping("/kickUser")
	    @ResponseBody
	    public ResponseEntity<String> kickUser(@RequestBody Map<String, String> payload) {
	        String chatRoomId = payload.get("chatRoomId");
	        String username = payload.get("username");
	        
	        if (chatRoomId == null || username == null) {
	            return ResponseEntity.badRequest().body("유효하지 않은 요청입니다.");
	        }

	        boolean success = chatRoomService.kickUser(chatRoomId, username);
	        if (success) {
	            return ResponseEntity.ok("유저가 강퇴되었습니다.");
	        } else {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("강퇴 실패");
	        }
	    }
	    
	    //채팅내용 갱신
		@GetMapping("/getMessages") 
		@ResponseBody 
		public List<ChatRoomDTO> getMessages(@RequestParam Long chatRoomId) { 
			return chatRoomService.getMessages(chatRoomId);
		}
}
