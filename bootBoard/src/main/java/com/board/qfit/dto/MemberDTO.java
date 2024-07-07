package com.board.qfit.dto;

public class MemberDTO {

    private String memberId;
    private String name;
    private String nickname;
    private String password;
    private String role;
    
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return "MemberDTO [memberId=" + memberId + ", name=" + name + ", nickname=" + nickname + ", password="
				+ password + ", role=" + role + "]";
	}
	
    
}
