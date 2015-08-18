package com.jy.domain.rest;

import com.jy.domain.Message;
import com.jy.service.UserService;
import com.jy.service.UserServiceImpl;

public class MessageVO extends Message{
	
	private UserService userService=new UserServiceImpl();
	
	private String senderName;
	
	private Boolean isFriend;

	public Boolean getIsFriend() {
		return isFriend;
	}

	public void setIsFriend(Boolean isFriend) {
		this.isFriend = isFriend;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
}
