package com.jy.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jy.dao.FriendDao;
import com.jy.dao.MessageDao;
import com.jy.dao.UserBlockDao;
import com.jy.dao.UserDao;
import com.jy.dao.UserMockDao;
import com.jy.domain.Friend;
import com.jy.domain.Message;
import com.jy.domain.MessageStatus;
import com.jy.domain.MessageType;
import com.jy.domain.User;
import com.jy.domain.rest.MessageDetailVO;
import com.jy.domain.rest.MessageVO;
import com.jy.exception.InvalidAttributesException;
import com.jy.utils.StringUtils;
import com.jy.utils.UploadUtils;
import com.jy.web.utils.ImageScale;


@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private FriendDao friendDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserMockDao userMockDao;
	
	@Autowired
	private UserBlockDao userBlockDao;
	
	@Autowired
	private PushNotificationHubService pushNotificationHubService;

	@Transactional
	private void sendMessage(User sender, Long receiver, String content, String type, String status, MultipartFile file, Integer width, Integer height) throws InvalidAttributesException{
		try {
			if (null==sender) {
				throw new InvalidAttributesException("Invalid parameter - sender");
			}
			if (null==receiver) {
				throw new InvalidAttributesException("Invalid parameter - receiver");
			}
			if (null!=userBlockDao.getByUserIdAndBlockedUser(receiver, sender.getId())) {
				throw new InvalidAttributesException("USER_BLOCKED");
			}
			//update active time
			sender.setLastlogintime(new Date());
			userDao.update(sender);
			Message msg = new Message();
			msg.setSenderId(sender.getId());
			msg.setReceiverId(receiver);
			if (null!=type && type.equals(MessageType.friendRequest.name())) {
				msg.setMsg(sender.getName() + " 请求加你为好友");
				msg.setMsgType("friendRequest");
				msg.setMsgStatus("inrequest");
			}else{
				if (null==content) {
					throw new InvalidAttributesException("Invalid parameter - content");
				}
				msg.setMsg(content);		
			}
			msg.setSendtime(new Date());
			msg.setIsRead(false);
			msg.setIsDeleted(false);
			msg.setIsReplied(false);
			String fileName = "";
			User receiverUser = userDao.getById(receiver);
			if (null != file) {
				fileName = file.getOriginalFilename();
				if (StringUtils.hasLength(fileName)) {
					String finalFileName= UploadUtils.INSTANCE.getUploadFileName(sender.getUuid(), fileName);
					ImageScale.scaleImage(file.getInputStream(), UploadUtils.INSTANCE.getUploadMessageFilePath() + finalFileName);
					String finalFileHttpPath=UploadUtils.INSTANCE.getUploadMessageHttpPath()+ finalFileName;
					msg.setSenderPic(finalFileHttpPath);
					msg.setSenderPicWidth(width);
					msg.setSenderPicHeight(height);
				}
			}else{
				if(!type.equals(MessageType.friendRequest.name())){
					if (receiverUser.getReceiveProfile()) {
						msg.setSenderPic(sender.getPic());
						msg.setSenderPicWidth(sender.getPicWidth());
						msg.setSenderPicHeight(sender.getPicHeight());
					}else{
						throw new InvalidAttributesException("NOT_RECEIVE_PROFILE");
					}
				}
			}
			messageDao.create(msg);
			//update mock user
			if (null!=userMockDao.getMockByUserId(receiver)) {
				receiverUser.setLastlogintime(new Date());
				userDao.update(receiverUser);
			}
			//push notification to ios device
			pushNotificationHubService.push(userDao.getById(receiver), msg.getMsg(),messageDao.getUserUnreadMessageCount(receiver));
		} catch (Exception e) {
			System.out.println("receiver: " + receiver);
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}
	
	@Override
	public void sendFriendRequestMsgToReceiver(User sender, Long receiverId) throws InvalidAttributesException {
		Message request = messageDao.getFriendRequestBySenderAndReceiver(sender.getId(), receiverId, MessageType.friendRequest.name(), MessageStatus.inrequest.name());
		if (null!=request) {
			throw new InvalidAttributesException("You already sent a request");
		}
		Friend friend = friendDao.getFriendExist(sender.getId(), receiverId);
		if (null!=friend) {
			throw new InvalidAttributesException("You are friends already");
		}
		sendMessage(sender, receiverId, null, MessageType.friendRequest.name() , null, null, null, null);
	}


	@Override
	public Integer getUserUnreadMessageCount(Long userId)
			throws InvalidAttributesException {
		return messageDao.getUserUnreadMessageCount(userId);
	}

	@Override
	public List<MessageVO> getUserMessages(Long userId, Integer pageNumber, Integer pageSize, Long timestamp)
			throws InvalidAttributesException {
		Date duetime = new Date(timestamp);
		return messageDao.getUserUnreadMessage(userId, (pageNumber - 1) * pageSize, pageSize, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(duetime));
	}


	@Override
	@Transactional
	public void sendCommonMsg(User sender, Long receiver, String content,
			MultipartFile file, Integer width, Integer height) throws InvalidAttributesException {
		sendMessage(sender, receiver, content, null, null, file, width, height);
	}

	@Override
	@Transactional
	public void deleteMessage(User receiver, Long msgId)
			throws InvalidAttributesException {
		try {
			Message message = messageDao.getById(msgId);
			if (null == message) {
				throw new InvalidAttributesException("Message not found");
			}
			if (!receiver.getId().equals(message.getReceiverId())) {
				throw new InvalidAttributesException("This is not your message");
			}
			message.setIsDeleted(true);
			message.setIsRead(true);
			if (null!=message.getMsgType()&&null!=message.getMsgStatus()) {
				if (message.getMsgType().equals(MessageType.friendRequest.name())&&message.getMsgStatus().equals(MessageStatus.inrequest.name())) {
					message.setMsgStatus(MessageStatus.rejected.name());
				}
			}
			messageDao.update(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public void acceptFriendRequest(User receiver, Long msgId)
			throws InvalidAttributesException {
		try {
			Message message = messageDao.getById(msgId);
			if (null == message) {
				throw new InvalidAttributesException("Message not found");
			}
			if (!receiver.getId().equals(message.getReceiverId())) {
				throw new InvalidAttributesException("This is not your message");
			}
			if (!message.getMsgType().equals(MessageType.friendRequest.name())) {
				throw new InvalidAttributesException("This is not a friend request");
			}
			if (!message.getMsgStatus().equals(MessageStatus.inrequest.name())) {
				throw new InvalidAttributesException("This request is expired");
			}
			if (null==friendDao.getFriendExist(message.getSenderId(), message.getReceiverId())) {
				Friend friend = new Friend();
				friend.setCreatetime(new Date());
				friend.setUserId(message.getSenderId());
				friend.setFriendId(message.getReceiverId());
				friendDao.create(friend);
			}
			message.setIsReplied(true);
			message.setIsRead(true);
			message.setMsgStatus(MessageStatus.accepted.name());
			messageDao.update(message);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public void rejectFriendRequest(User receiver, Long msgId)
			throws InvalidAttributesException {
		try {
			Message message = messageDao.getById(msgId);
			if (null == message) {
				throw new InvalidAttributesException("Message not found");
			}
			if (!receiver.getId().equals(message.getReceiverId())) {
				throw new InvalidAttributesException("This is not your message");
			}
			if (!message.getMsgType().equals(MessageType.friendRequest.name())) {
				throw new InvalidAttributesException("This is not a friend request");
			}
			if (!message.getMsgStatus().equals(MessageStatus.inrequest.name())) {
				throw new InvalidAttributesException("This request is expired");
			}
			message.setIsReplied(true);
			message.setIsRead(true);
			message.setMsgStatus(MessageStatus.rejected.name());
			messageDao.update(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public void replyMsg(User sender, String content,
			MultipartFile file, Integer width, Integer height, Long messageId)
			throws InvalidAttributesException {
		try {
			Message message = messageDao.getById(messageId);
			if (null == message) {
				throw new InvalidAttributesException("Cannot find the message");
			}
			if (!sender.getId().equals(message.getReceiverId())) {
				throw new InvalidAttributesException("You are not the receiver");
			}
			message.setIsReplied(true);
			message.setIsRead(true);
			messageDao.update(message);
			sendMessage(sender, message.getSenderId(), content, null, null, file, width, height);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}

	@Override
	public List<MessageDetailVO> getTodaysMessages() {
		return messageDao.getTodaysMessages();
	}

	@Override
	public Integer getMessageCount(Long userId, Long theOtherUserId) {
		return messageDao.getAllMessagesCount(userId, theOtherUserId);
	}

	@Override
	public List<MessageDetailVO> getMessage(Long userId, Long theOtherUserId, Integer pageNumber, Integer pageSize) {
		return messageDao.getAllMessages(userId, theOtherUserId, (pageNumber - 1) * pageSize, pageSize);
	}

	@Override
	public void deleteMessage(Long messageId) throws InvalidAttributesException {
		try {
			Message message = messageDao.getById(messageId);
			if (null == message) {
				throw new InvalidAttributesException("Message not found");
			}
			message.setIsDeleted(true);
			message.setIsRead(true);
			if (null!=message.getMsgType()&&null!=message.getMsgStatus()) {
				if (message.getMsgType().equals(MessageType.friendRequest.name())&&message.getMsgStatus().equals(MessageStatus.inrequest.name())) {
					message.setMsgStatus(MessageStatus.rejected.name());
				}
			}
			messageDao.update(message);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}

	@Override
	public Message getMessageById(Long messageId)
			throws InvalidAttributesException {
		if (null == messageId) {
			throw new InvalidAttributesException("Message Id is empty");
		}
		return messageDao.getById(messageId);
	}

}
 