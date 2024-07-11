package com.board.qfit.dto;

import lombok.Getter;
import lombok.Setter;

/* 사용자가 입력한 폼 데이터 내용을 컨트롤러로 전달하기 위한 클래스*/
@Getter
@Setter
public class ChatRoomForm {
	private String name;
    private String password;
    private int limit;
    private String host;
}
