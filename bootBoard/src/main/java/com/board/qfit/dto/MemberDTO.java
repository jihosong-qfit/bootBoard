package com.board.qfit.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {

    private String memberId;
    private String name;
    private String nickname;
    private String password;
    private String role;
    
}
