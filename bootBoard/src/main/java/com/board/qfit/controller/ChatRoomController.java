package com.board.qfit.controller;

import java.util.HashMap;
import java.util.Iterator;
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
import com.board.qfit.dto.ChatUser;
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
	    	System.out.println("Creating chat room with host: " + memberId);
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
	    	//2. 채팅방 입장 시 유저 정보를 DB에 저장
	    	chatRoomService.addUserToChatRoom(memberId, username);
//	    	List<ChatUser> chatUsers = chatRoomService.getChatUser();
//	    	for (int i=0; i<chatUsers.size(); i++) {
//	    		System.out.println(chatUsers.get(i).getUserId());
//	    	}
	    	//3. 채팅방 입장 시 상세 정보
	        ChatRoomDTO chatRoom = chatRoomService.chatRoom(id);
	        //4. 채팅방 접속자 목록 가져오기
	        List<MemberDTO> members = chatRoomService.getUsersInRoom(id);
	        //5. 채팅방 정보와 멤버 목록, 채팅방 유저 정보 model에 추가
	        model.addAttribute("chatRoom", chatRoom);
	        model.addAttribute("members", members);
//	        model.addAttribute("chatUsers", chatUsers);
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
	    	List<MemberDTO> members = chatRoomService.getUsersInRoom(chatRoomId);
	    	System.out.println("Members: " + members);
	    	return members;
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
	    //map을 파라미터로 넘겼을 때 map 안의 파라미터가 null인경우 혹은 넘어가는 데이터 타입이 다른 경우 오류이다!
	    @PostMapping("/kickUser")
	    @ResponseBody
	    public String kickUser(@RequestParam("userId") String userId) {
	    	
	        if (userId == null || userId.isEmpty()) {
	            return "에러: userId is null or empty";
	        }
	        
	        try {
	            chatRoomService.kickUser(userId);    
	        } catch (Exception e) {
	            return "에러: " + e.getMessage();
	        }
	        
	        return "success";
	    }
	    
	    //채팅내용 갱신
		@GetMapping("/getMessages") 
		@ResponseBody 
		public List<ChatRoomDTO> getMessages(@RequestParam Long chatRoomId) { 
			return chatRoomService.getMessages(chatRoomId);
		}
		
		//채팅방 입장시 비밀번호 유효성 검증
		@PostMapping("/validatePassword")
	    @ResponseBody
	    public ResponseEntity<Map<String, Boolean>> validatePassword
	    						(@RequestBody Map<String, Object> payload) {
			
			//채팅방 목록 화면에서 roomId와 password를 요청받는다.
			Object roomIdObj = payload.get("roomId"); //roomId를 object로 받는다.
	        Long roomId;
	        if (roomIdObj instanceof Integer) {
	            roomId = ((Integer) roomIdObj).longValue(); //object로 받은 roomId를 Integer형과 비교해서 long value로 받는다.
	        } else if (roomIdObj instanceof String) {
	            roomId = Long.valueOf((String) roomIdObj);
	        } else {
	            return ResponseEntity.badRequest().build(); // roomId가 예상한 타입이 아닌 경우에 대한 처리
	        }
	        String password = (String) payload.get("password");
	        //비밀번호 유효성 검증을 해서 boolean값을 반환
	        boolean isValid = chatRoomService.validatePassword(roomId, password);
	        
	        //map에 valid를 key에 비밀번호 유효성 검증 로직을 마친 값을 넣음
	        Map<String, Boolean> response = new HashMap<>();
	        response.put("valid", isValid);
	        
	        //map을 화면으로 반환
	        return ResponseEntity.ok(response);
	    }
}
