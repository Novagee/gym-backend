package com.jy.service;

import org.springframework.stereotype.Service;

import com.jy.domain.User;
import com.jy.exception.InvalidAttributesException;

@Service
public interface PushNotificationHubService {
	
	public void push(User user, String content, int badge) throws InvalidAttributesException;
	
}
