package com.board.qfit.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomDTO {
	private Long id; /* 채팅방 id */
	private String name; /* 채팅방 이름 */
	private String host; /* 방장 */
	private int limit; /* 인원수 제한 */
	private String password;
	private int connectedUsers; /* 채팅방 접속자수 */
	private List<MemberDTO> members; /* 접속한 유저 목록*/
	private String sender;
	private String recipient;
	private String message;
	private boolean isWhisper;
	
	public ChatRoomDTO() {}
	
	//일반 메시지 전송 생성자
	public ChatRoomDTO(Long id, String sender, String message, boolean isWhisper) {
		this.id = id;
		this.sender = sender;
		this.message = message;
		this.isWhisper = isWhisper; //일반메시지 기본 설정
	}
	//귓속말 전송 생성자
	public ChatRoomDTO(Long id, String sender, String message, String recipient, boolean isWhisper) {
		this.id = id;
		this.sender = sender;
		this.message = message;
		this.recipient = recipient;
		this.isWhisper = isWhisper;
	}
}
