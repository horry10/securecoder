package com.spring_tutorial.board.model.dto;

import java.sql.Date;

public class MemberDto {
	private String userId;
	private String userPw;
	private String userName;
	private Date regDate;

	public MemberDto() {

	}
	
	public MemberDto(String userId, String userPw) {
		this.userId = userId;
		this.userPw = userPw;
	}

	public MemberDto(String userId, String userPw, String userName) {
		this.userId = userId;
		this.userPw = userPw;
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	
}
