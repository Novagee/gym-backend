package com.jy.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jy.dao.UserDao;
import com.jy.domain.User;
import com.jy.exception.InvalidAttributesException;
import com.jy.utils.StringUtils;
import com.jy.utils.UploadUtils;
import com.tencent.xinge.ClickAction;
import com.tencent.xinge.Message;
import com.tencent.xinge.Style;
import com.tencent.xinge.TimeInterval;
import com.tencent.xinge.XingeApp;

@Service
public class AndroidPushNotificationServiceImpl implements
AndroidPushNotificationService {

	@Autowired
	private UserDao userDao;

	@Override
	@Transactional
	public void pushNotification(User receiver, String content, int badge)
			throws InvalidAttributesException {
		try {
			String accessId=UploadUtils.INSTANCE.getAndroidAccessId();
			String secretKey=UploadUtils.INSTANCE.getAndroidSecretKey();
			if (StringUtils.hasLength(accessId)) {
				XingeApp xinge = new XingeApp(Long.parseLong(accessId), secretKey);
				Message message = new Message();
				message = new Message();
				message.setType(Message.TYPE_NOTIFICATION);
				Style style = new Style(1);
				style = new Style(3,1,0,1,0);
				ClickAction action = new ClickAction();
				action.setActionType(ClickAction.TYPE_ACTIVITY);
				Map<String, Object> custom = new HashMap<String, Object>();
				message.setTitle("简约");
				message.setContent(content);
				message.setStyle(style);
				message.setAction(action);
				message.setCustom(custom);
				TimeInterval acceptTime1 = new TimeInterval(0,0,23,59);
				message.addAcceptTime(acceptTime1);
				if (StringUtils.hasLength(receiver.getDeviceToken())) {
					System.out.println(xinge.pushSingleDevice(receiver.getDeviceToken(), message));
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new InvalidAttributesException("Invalid accessId");
		}
	}

	/*
	public static void main(String[] args) {
		String[] devices = {
				"efa4bfafcc1663a5ea1b8ee9455faae6c920b7c3fd425152e36fd483498a25db" };

		try {

			List<PushedNotification> notifications = Push.alert("Hello World!",
					"E:/career/ties/ties/src/main/resources/Certificates.p12", "ties", true, devices);

			for (PushedNotification notification : notifications) {
				if (notification.isSuccessful()) {
					System.out
							.println("Push notification sent successfully to: "
									+ notification.getDevice().getToken());
				} else {
					String invalidToken = notification.getDevice().getToken();

					Exception theProblem = notification.getException();
					theProblem.printStackTrace();

					ResponsePacket theErrorResponse = notification
							.getResponse();
					if (theErrorResponse != null) {
						System.out.println(theErrorResponse.getMessage());
					}
				}
			}

		} catch (KeystoreException e) {
			e.printStackTrace();

		} catch (CommunicationException e) {
			e.printStackTrace();
		}
	}*/

}
