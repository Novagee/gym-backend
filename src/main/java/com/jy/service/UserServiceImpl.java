package com.jy.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jy.dao.FriendDao;
import com.jy.dao.MessageDao;
import com.jy.dao.UserBlockDao;
import com.jy.dao.UserDao;
import com.jy.dao.UserLocationDao;
import com.jy.domain.Friend;
import com.jy.domain.Message;
import com.jy.domain.User;
import com.jy.domain.UserBlock;
import com.jy.domain.UserLocation;
import com.jy.domain.rest.UserVO;
import com.jy.exception.InvalidAttributesException;
import com.jy.utils.StringUtils;
import com.jy.utils.UploadUtils;
import com.jy.web.utils.ImageScale;
import com.jy.web.validator.UserFormValidator;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private FriendDao friendDao;
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private UserLocationDao userLocationDao;
	
	@Autowired
	private UserFormValidator userFormValidator;
	
	@Autowired
	private UserBlockDao userBlockDao;

	@Override
	public User getById(Long id) {
		return userDao.getById(id);
	}

	@Override
	@Transactional
	public User signin(String name, String deviceToken, String deviceType, String gender, String interestIn, MultipartFile file, Integer width, Integer height, 
			String lat, String lng) throws InvalidAttributesException {
		try {
			User user = new User();
			user.setName(name);
			user.setEnabled(true);
			user.setLastlogintime(new Date());
			user.setCreatetime(new Date());
			user.setDeviceToken(deviceToken);
			user.setDeviceType(StringUtils.hasLength(deviceType)?deviceType:"iOS");
			user.setUuid(UUID.randomUUID().toString());
			user.setEnabled(true);
			user.setGender(gender);
			user.setInterestIn(interestIn);
			user.setLikes(0);
			user.setReceiveProfile(false);
			String fileName = "";
			if (null != file) {
				fileName = file.getOriginalFilename();
				if (StringUtils.hasLength(fileName)) {
					String finalFileName= UploadUtils.INSTANCE.getUploadFileName(user.getUuid(), fileName);
					ImageScale.scaleImage(file.getInputStream(), UploadUtils.INSTANCE.getUploadProfileFilePath() + finalFileName);
					String finalFileHttpPath=UploadUtils.INSTANCE.getUploadprofileHttpPath()+ finalFileName;
					user.setPic(finalFileHttpPath);
					user.setPicWidth(width);
					user.setPicHeight(height);
				}
			}
			userDao.create(user);
			if (StringUtils.hasLength(lat)&&StringUtils.hasLength(lng)) {
				Double latitude = null;
				Double longitude = null;
				if(StringUtils.hasLength(lat)&&StringUtils.hasLength(lng)){
					latitude = Double.parseDouble(lat);
					longitude = Double.parseDouble(lng);
				}
				UserLocation location = new UserLocation();
				location.setUserId(user.getId());
				location.setLat(latitude);
				location.setLng(longitude);
				location.setUpdatetime(new Date());
				userLocationDao.create(location);
			}
			return user;
		} catch (IOException e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public void updateLocation(Long userId, String lat, String lng)
			throws InvalidAttributesException {
		try {
			UserLocation location = userLocationDao.getUserLocation(userId);
			if (null!=location) {
				Double latitude = null;
				Double longitude = null;
				if(StringUtils.hasLength(lat)&&StringUtils.hasLength(lng)){
					latitude = Double.parseDouble(lat);
					longitude = Double.parseDouble(lng);
				}
				location.setLat(latitude);
				location.setLng(longitude);
				location.setUpdatetime(new Date());
				userLocationDao.update(location);
			}else{
				location = new UserLocation();
				Double latitude = null;
				Double longitude = null;
				if(StringUtils.hasLength(lat)&&StringUtils.hasLength(lng)){
					latitude = Double.parseDouble(lat);
					longitude = Double.parseDouble(lng);
				}
				location.setLat(latitude);
				location.setLng(longitude);
				location.setUpdatetime(new Date());
				location.setUserId(userId);
				userLocationDao.create(location);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new InvalidAttributesException("Lat and lng should be numbers");
		}
	}

	@Override
	public User updateUser(User user, String gender, String interestIn,
			String lat, String lng, MultipartFile file, Integer width, Integer height, String lastLoginTime, String receiveProfile, String tagline)
			throws InvalidAttributesException {
		try {
			user.setLastlogintime(new Date());
			userFormValidator.validateGender(gender);
			user.setGender(gender);
			userFormValidator.validateGender(interestIn);
			user.setInterestIn(interestIn);
			String fileName = "";
			if (null != file) {
				fileName = file.getOriginalFilename();
				if (StringUtils.hasLength(fileName)) {
					String finalFileName= UploadUtils.INSTANCE.getUploadFileName(user.getUuid(), fileName);
					ImageScale.scaleImage(file.getInputStream(), UploadUtils.INSTANCE.getUploadProfileFilePath() + finalFileName);
					String finalFileHttpPath=UploadUtils.INSTANCE.getUploadprofileHttpPath()+ finalFileName;
					user.setPic(finalFileHttpPath);
				}
			}
			user.setPicWidth(width);
			user.setPicHeight(height);
			user.setReceiveProfile(receiveProfile.equals("Y"));
			user.setTagline(tagline);
			if (StringUtils.hasLength(lastLoginTime)) {
				user.setLastlogintime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastLoginTime));
			}
			updateLocation(user.getId(), lat, lng);
			userDao.update(user);
			return user;
		} catch (IOException e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		} catch (ParseException e) {
			e.printStackTrace();
			throw new InvalidAttributesException("time format should be yyyy-MM-dd HH:mm:ss");
		}
	}

	@Override
	public List<UserVO> getUser(Long userId, String interestIn, Double lat,
			Double lng, Integer pageNumber, Integer pageSize, Long timestamp)
			throws InvalidAttributesException {
		try {
			if (!interestIn.equals("M")&&!interestIn.equals("F")) {
				interestIn = null;
			}
			Date duetime = new Date(timestamp);
			return userDao.getUser(userId, interestIn, lat, lng, (pageNumber-1)*pageSize, pageSize, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(duetime));
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}

	@Override
	public List<UserVO> getTodaysUsers() {
		return userDao.getTodaysUsers();
	}

	@Override
	public Integer getUserCount(String userId, String name, String deviceType, String gender) {
		String uid=null;
		String uname = null;
		String dt = null;
		String g = null;
		if (StringUtils.hasLength(userId)) {
			uid = "%"+userId+"%";
		}
		if (StringUtils.hasLength(name)) {
			uname = "%"+name+"%";
		}
		if (StringUtils.hasLength(deviceType)) {
			dt = "%"+deviceType+"%";
		}
		if (StringUtils.hasLength(gender)) {
			g = "%"+gender+"%";
		}
		return userDao.getAllUserCount(uid, uname, dt, g);
	}

	@Override
	public List<UserVO> getUser(String userId, String name, String deviceType, String gender, Integer pageNumber, Integer pageSize)
			throws InvalidAttributesException {
		String uid=null;
		String uname = null;
		String dt = null;
		String g = null;
		if (StringUtils.hasLength(userId)) {
			uid = "%"+userId+"%";
		}
		if (StringUtils.hasLength(name)) {
			uname = "%"+name+"%";
		}
		if (StringUtils.hasLength(deviceType)) {
			dt = "%"+deviceType+"%";
		}
		if (StringUtils.hasLength(gender)) {
			g = "%"+gender+"%";
		}
		return userDao.getAllUser(uid, uname, dt, g, (pageNumber-1)*pageSize, pageSize);
	}

	@Override
	public void enableUser(Long userId, boolean enabled)
			throws InvalidAttributesException {
		try {
			if (null==userId) {
				throw new InvalidAttributesException("User id is empty");
			}
			User user = userDao.getById(userId);
			user.setEnabled(enabled);
			userDao.update(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}

	@Override
	public UserVO getDetailById(Long id) {
		return userDao.getDetailById(id);
	}

	@Override
	@Transactional
	public void transferData(Long fromUser, Long toUser)
			throws InvalidAttributesException {
		try {
			if (null==fromUser) {
				throw new InvalidAttributesException("Please choose the original user.");
			}
			if (null==toUser) {
				throw new InvalidAttributesException("Please choose the target user.");
			}
			for (Friend friend : friendDao.getFriends(fromUser)) {
				if (friend.getUserId().equals(fromUser)) {
					friend.setUserId(toUser);
				}
				if (friend.getFriendId().equals(fromUser)) {
					friend.setFriendId(toUser);
				}
				friendDao.update(friend);
			}
			for (Message message : messageDao.getMessages(fromUser)) {
				if (message.getSenderId().equals(fromUser)) {
					message.setSenderId(toUser);
				}
				if (message.getReceiverId().equals(fromUser)) {
					message.setReceiverId(toUser);
				}
				messageDao.update(message);
			}
			User original = userDao.getById(fromUser);
			original.setEnabled(false);
			userDao.update(original);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public void blockUser(User user, String userBlocked)
			throws InvalidAttributesException {
		try {
			if (null==userBlocked) {
				throw new InvalidAttributesException("Blocked user is empty");
			}
			UserBlock ub = new UserBlock();
			ub.setUserId(user.getId());
			ub.setUserBlocked(Long.parseLong(userBlocked));
			ub.setCreatetime(new Date());
			userBlockDao.create(ub);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}
	
}
