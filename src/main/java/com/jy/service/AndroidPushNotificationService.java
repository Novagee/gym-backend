package com.jy.service;

import org.springframework.stereotype.Service;

import com.jy.domain.User;
import com.jy.exception.InvalidAttributesException;

@Service
public interface AndroidPushNotificationService {
	public void pushNotification(User receiver, String content, int badge)
			throws InvalidAttributesException;
}
