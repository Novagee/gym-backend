package com.jy.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jy.domain.User;
import com.jy.domain.rest.UserVO;
import com.jy.exception.InvalidAttributesException;

@Service
public interface UserService {

	public User getById(Long id);

	public UserVO getDetailById(Long id);

	public User signin(String name, String deviceToken, String deviceType,
			String gender, String interestIn, MultipartFile file,
			Integer width, Integer height, String lat, String lng)
			throws InvalidAttributesException;

	public User updateUser(User user, String gender, String interestIn,
			String lat, String lng, MultipartFile file, Integer width,
			Integer height, String lastLoginTime, String receiveProfile, String tagline)
			throws InvalidAttributesException;
	
	public void blockUser(User user, String userBlocked)
			throws InvalidAttributesException;

	public List<UserVO> getUser(Long userId, String interestIn, Double lat,
			Double lng, Integer pageNumber, Integer pageSize, Long timestamp)
			throws InvalidAttributesException;

	public void updateLocation(Long userId, String lat, String lng)
			throws InvalidAttributesException;

	public List<UserVO> getTodaysUsers();

	public Integer getUserCount(String userId, String name, String deviceType,
			String gender);

	public List<UserVO> getUser(String userId, String name, String deviceType,
			String gender, Integer pageNumber, Integer pageSize)
			throws InvalidAttributesException;

	public void enableUser(Long userId, boolean enabled)
			throws InvalidAttributesException;

	public void transferData(Long fromUser, Long toUser)
			throws InvalidAttributesException;
}
