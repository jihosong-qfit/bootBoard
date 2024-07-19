package com.board.qfit.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {
    private String memberId; /*로그인id*/
    private String name; /*유저 이름*/
    private String nickname;
    private String password;
    private String role;
    private int isConnected; //접속여부
    
    private String userId;
    private String username;
}
