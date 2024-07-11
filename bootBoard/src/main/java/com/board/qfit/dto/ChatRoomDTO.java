package com.board.qfit.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomDTO {
	
	private Long id;
	private String name;
	private String host;
	private int limit;
	private String password;
	private int connectedUsers;
	
}
