package com.jy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jy.domain.DeviceType;
import com.jy.domain.User;
import com.jy.exception.InvalidAttributesException;

@Service
public class PushNotificationHubServiceImple implements PushNotificationHubService{
	
	@Autowired
	private ApplePushNotificationService appleService;
	
	@Autowired
	private AndroidPushNotificationService androidService;
	
	@Transactional
	public void push(User user, String content, int badge) throws InvalidAttributesException{
		if (user.getDeviceType().equalsIgnoreCase(DeviceType.iOS.name())) {
			appleService.pushNotification(user, content, badge);
		}
		if (user.getDeviceType().equalsIgnoreCase(DeviceType.android.name())) {
			androidService.pushNotification(user, content, badge);
		}
	}
	
}
