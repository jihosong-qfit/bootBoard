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

import com.board.qfit.dto.BoardDTO;
import com.board.qfit.dto.ChatRoomDTO;
import com.board.qfit.dto.ChatRoomForm;
import com.board.qfit.dto.ChatUser;
import com.board.qfit.dto.MemberDTO;
import com.board.qfit.service.ChatRoomService;
import com.board.qfit.service.MemberService;
import com.board.qfit.util.SessionManager;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatRoomService chatRoomService;
	private final MemberService memberService;
	private final SessionManager sessionManager;

	// 채팅방 생성 화면
	@GetMapping("/chatCreate")
	public String createChatRoomForm(Model model, HttpSession session) {
		// 게시판 목록 session에 저장되어있던 로그인 아이디값을 가져온다.
		String memberId = (String) session.getAttribute("memberId");
		// 로그인한 아이디값이 없으면 로그인 페이지로 이동한다.
		if (memberId == null) {
			return "redirect:/member/login";
		}

		ChatRoomForm chatRoomForm = new ChatRoomForm();
		chatRoomForm.setHost(memberId); // session에서 가져온 로그인한 사용자 아이디값을 방장으로 설정
		model.addAttribute("chatRoomForm", chatRoomForm);

		return "chat/chatCreate";
	}

	// 채팅방 생성
	@PostMapping("/chatCreate")
	public String createChatRoom(
			@ModelAttribute ChatRoomForm chatRoomForm,
			HttpSession session,
			Model model) {
		// 사용자에게 입력받은 채팅방정보를 바탕으로 채팅방 생성
		// 게시판 목록 session에 저장되어있던 로그인 아이디값을 가져온다.
		String memberId = (String) session.getAttribute("memberId");
		if (memberId == null) {
			return "redirect:/member/login";
		}

		chatRoomForm.setHost(memberId); // session에서 가져온 로그인한 사용자 아이디값을 방장으로 설정
		System.out.println("채팅방 생성 방장: " + memberId);
		Long id = chatRoomService.chatCreate(chatRoomForm);
		System.out.println("채팅방 생성 후 리디렉션 ID: " + id);
		return "redirect:/chat/chatRoom/" + id;
	}

	// 채팅방 목록 화면
	@GetMapping("/chatList")
	public String chatListPage(Model model, HttpSession session) {
		String memberId = (String) session.getAttribute("memberId");
		List<ChatRoomDTO> chatRooms = chatRoomService.chatList();
		model.addAttribute("chatRooms", chatRooms);
		return "chat/chatList";
	}

	// 채팅방 목록 실시간 업데이트
	@GetMapping("/getChatList")
	@ResponseBody
	public List<ChatRoomDTO> getChatList(HttpSession session) {
		String memberId = (String) session.getAttribute("memberId");

		return chatRoomService.chatList();
	}

	// 채팅방 상세 조회 화면
	@GetMapping("/chatRoom/{id}")
	public String getChatRoom(@PathVariable Long id, Model model, HttpSession session) {
		System.out.println("채팅방 상세 조회 ID: " + id);
		String memberId = (String) session.getAttribute("memberId");
		String username = (String) session.getAttribute("username");

		Boolean isKicked = (Boolean) session.getAttribute("kicked");
		if (isKicked != null && isKicked) {
			session.removeAttribute("kicked"); // 플래그 제거
			return "redirect:/chat/chatList"; // 채팅방 목록으로 리다이렉트
		}

		// 채팅방 접근 시 저장되어있던 비밀번호 검증 session 가져오기
		Boolean isValidated = (Boolean) session.getAttribute("chatRoomId" + id + "validatPassword");
		// 1. 접속자수 관리
		chatRoomService.updateConnectionStatus(id, memberId, true); // true일 때 1, false면 0
		// 2. 채팅방 입장 시 유저 정보를 DB에 저장 => 채팅방 입장하기 버튼을 눌렀을 때 비밀번호가 있는 경우 없는 경우 실시간 처리르 위해
		// ajax 처리로 변경
//	    	chatRoomService.addUserToChatRoom(memberId, username);
		// 3. 채팅방 입장 시 상세 정보
		ChatRoomDTO chatRoom = chatRoomService.chatRoom(id);
		// 4. 채팅방 접속자 목록 가져오기
		List<MemberDTO> members = chatRoomService.getUsersInRoom(id);
		// 5. 채팅방 정보와 멤버 목록, 채팅방 유저 정보 model에 추가
		model.addAttribute("chatRoom", chatRoom);
		model.addAttribute("members", members);

		if (isValidated != null && isValidated) { // 비밀번호가 있는 채팅방의 경우
			return "chat/chatRoom";
		} else {
			return "chat/chatRoom"; // 비밀번호가 없는 채팅방의 경우
		}
	}

	// 채팅내용 전송
	// 사용자의 메시지, 채팅방 id를 받아옴
	@PostMapping("/sendMessage")
	@ResponseBody
	public ChatRoomDTO sendMessage(@RequestBody Map<String, Object> payload) {
		String message = (String) payload.get("message");
		Long chatRoomId = Long.valueOf(payload.get("chatRoomId").toString());
		String sender = (String) payload.get("sender");

		if (message == null || message.trim().isEmpty() || chatRoomId == null || sender == null
				|| sender.trim().isEmpty()) {
			throw new IllegalArgumentException("message, chatRoomId, sender null 또는 빈 문자열");
		}

		chatRoomService.sendMessage(chatRoomId, message, sender);
		return new ChatRoomDTO(chatRoomId, message, sender, null, false);
	}

	// 귓속말버튼
	// 귓속말 대상자 유저, 채팅내용, id를 받아옴
	@PostMapping("/sendWhisper")
	@ResponseBody
	public ChatRoomDTO sendWhisper(@RequestBody Map<String, Object> payload) {

		String sender = (String) payload.get("sender");
		String recipient = (String) payload.get("recipient");
		String message = (String) payload.get("message");
		Long chatRoomId = Long.valueOf(payload.get("chatRoomId").toString());

		if (sender == null || sender.trim().isEmpty() || recipient == null || recipient.trim().isEmpty()
				|| message == null || message.trim().isEmpty() || chatRoomId == null) {
			throw new IllegalArgumentException("sender, recipient, message, chatRoomId null 또는 빈 문자열");
		}

		MemberDTO senderMember = new MemberDTO();
		senderMember.setMemberId(sender); // 보내는 사람: 유저id

		MemberDTO recipientMember = new MemberDTO();
		recipientMember.setName(recipient); // 받는 사람: 유저명

		chatRoomService.sendWhisper(chatRoomId, sender, recipient, message);
		ChatRoomDTO response = new ChatRoomDTO(chatRoomId, senderMember.getMemberId(), recipientMember.getName(),
				message, true);
		return response;
	}

	// 강퇴버튼
	// 채팅방 id, 유저이름을 받아옴
	// 부적합한 열 유형: 1111
	// =>map을 파라미터로 넘겼을 때 map 안의 파라미터가 null인경우 혹은 넘어가는 데이터 타입이 다른 경우 오류이다!
	@PostMapping("/kickUser")
	@ResponseBody
	public String kickUser(@RequestParam("userId") String userId, @RequestParam("chatRoomId") Long chatRoomId) {

		if (userId == null || userId.isEmpty()) {
			return "에러: userId is null or empty";
		}

		try {
			chatRoomService.kickUser(userId, chatRoomId);
		} catch (Exception e) {
			return "에러: " + e.getMessage();
		}

		// 강제 퇴장된 유저의 세션에 플래그 설정
		HttpSession session = sessionManager.getSession(userId);
		if (session != null) {
			session.setAttribute("kicked", true);
		}

		return "success";
	}

	// 강제퇴장 여부 확인
	@GetMapping("/checkKickStatus")
	@ResponseBody
	public Map<String, Boolean> checkKickStatus(@RequestParam("userId") String userId) {
		HttpSession session = sessionManager.getSession(userId);
		Boolean kicked = session != null && (Boolean) session.getAttribute("kicked");
		Map<String, Boolean> response = new HashMap<>();
		response.put("kicked", kicked != null && kicked);
		return response;
	}

	// 접속자 목록 관리
	@GetMapping("/getUsersInRoom")
	@ResponseBody
	public List<MemberDTO> getUsersInRoom(@RequestParam Long chatRoomId) {
		if (chatRoomId == null) {
	        throw new IllegalArgumentException("getUsersInRoom chatRoomId is null");
	    }
		List<MemberDTO> members = chatRoomService.getUsersInRoom(chatRoomId);
		return members;
	}

	// 채팅내용 갱신
	@GetMapping("/getMessages")
	@ResponseBody
	public List<ChatRoomDTO> getMessages(@RequestParam Long chatRoomId) {
		if (chatRoomId == null) {
	        throw new IllegalArgumentException("getMessages chatRoomId is null");
	    }
		return chatRoomService.getMessages(chatRoomId);
	}

	// 채팅방 입장시 비밀번호 유효성 검증
	@PostMapping("/validatePassword")
	@ResponseBody
	public ResponseEntity<Map<String, Boolean>> validatePassword(@RequestBody Map<String, Object> payload,
			HttpSession session) {

		// 채팅방 목록 화면에서 roomId와 password를 요청받는다.
		Object roomIdObj = payload.get("roomId"); // roomId를 object로 받는다.
		Long roomId;
		if (roomIdObj instanceof Integer) {
			roomId = ((Integer) roomIdObj).longValue(); // object로 받은 roomId를 Integer형과 비교해서 long value로 받는다.
		} else if (roomIdObj instanceof String) {
			roomId = Long.valueOf((String) roomIdObj);
		} else {
			return ResponseEntity.badRequest().build(); // roomId가 예상한 타입이 아닌 경우에 대한 처리
		}

		String password = (String) payload.get("password");
		// 비밀번호 유효성 검증을 해서 boolean값을 반환
		boolean isValid = chatRoomService.validatePassword(roomId, password);
		// 비밀번호를 url에 노출하지 않고 session에 저장하고 서버에서만 검증
		if (isValid) {
			session.setAttribute("chatRoomId" + roomId + "validatPassword", true);
		}

		// map에 valid를 key에 비밀번호 유효성 검증 로직을 마친 값을 넣음
		Map<String, Boolean> response = new HashMap<>();
		response.put("valid", isValid);

		// map을 화면으로 반환
		return ResponseEntity.ok(response);
	}

	// 특정 채팅방 접속자 수와 최대 접속 가능 인원 조회
	@GetMapping("/getConnectedUsers")
	@ResponseBody
	public Map<String, Object> getConnectedUsers(@RequestParam Long roomId, @RequestParam String memberId) {
		// 채팅방 정보
		ChatRoomDTO chatRoom = chatRoomService.chatRoom(roomId);

		// 채팅방에 이미 접속한 유저 수 반환
		boolean alreadyConnected = chatRoomService.checkUserExists(roomId, memberId);

		Map<String, Object> response = new HashMap<>();
		response.put("connectedUsers", chatRoomService.getConnectedUsers(roomId)); // 접속자 수
		response.put("limit", chatRoomService.getUserLimit(roomId)); // 최대 접속 가능 인원
		response.put("alreadyConnected", alreadyConnected); // 이미 접속한 유저인지의 여부

		return response;
	}

	// 로그인 한 사용자를 채팅방에 입장
	@PostMapping("/enterRoom")
	@ResponseBody
	public ResponseEntity<?> enterRoom(@RequestBody Map<String, Object> params) {
		Long roomId = Long.valueOf(params.get("roomId").toString());
		String memberId = params.get("memberId").toString();

		// 관리자인지 확인
		boolean isAdmin = memberService.isAdmin(memberId);

		// 관리자가 아닌 경우에 접속자 수 증가
		if (!isAdmin) {
			boolean success = chatRoomService.enterRoom(roomId, memberId);
			if (!success) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("채팅방 입장에 실패했습니다. 인원 제한수를 초과하였습니다!");
			}
		}

		return ResponseEntity.ok().body("입장 성공");
	}

	// 채팅방 나가기 버튼
	@PostMapping("/leaveRoom")
	@ResponseBody
	public ResponseEntity<?> leaveRoom(@RequestBody Map<String, Object> params) {
		Long chatRoomId = Long.valueOf(params.get("chatRoomId").toString());
		String memberId = params.get("memberId").toString();

		// false로 설정했을때 접속상태는 0으로
		chatRoomService.updateConnectionStatus(chatRoomId, memberId, false);
		// 나가기
		chatRoomService.leaveRoom(chatRoomId, memberId);
		return ResponseEntity.ok().build();
	}

	// 채팅메시지, 채팅전송자로 선택하여 검색
	@GetMapping("/search")
	public String search(
			@RequestParam(defaultValue = "1") int page, 
			@RequestParam(defaultValue = "10") int size,
			@RequestParam String searchType, 
			@RequestParam String keyword, Model model, HttpSession session) {

		String memberId = (String) session.getAttribute("memberId");
		List<ChatRoomDTO> chatMessageList = chatRoomService.searchByMessageSender(page, size, searchType, keyword);
		int totalRecords = chatMessageList.size();
		int totalPages = (int) Math.ceil((double) totalRecords / size);

		model.addAttribute("chatMessageList", chatMessageList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("memberId", memberId);
		return "board/list";
	}

}
