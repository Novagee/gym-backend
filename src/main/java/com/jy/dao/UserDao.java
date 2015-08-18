package com.jy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jy.dataaccess.dao.GenericDao;
import com.jy.domain.User;
import com.jy.domain.rest.UserVO;

public interface UserDao extends GenericDao<User>{
	User getById(@Param("id") Long id);
	UserVO getDetailById(@Param("id") Long id);
	User getByUUID(@Param("uuid") String uuid);
	List<UserVO> getTodaysUsers();
	List<UserVO> getUser(@Param("userId") Long userId, @Param("interestIn") String interestIn, @Param("lat") Double lat, @Param("lng") Double lng, @Param("firstResult") Integer firstResult,  @Param("pageSize") Integer pageSize, @Param("timestamp") String timestamp);
	List<UserVO> getAllUser(@Param("userId")String userId, @Param("name")String name, @Param("deviceType")String deviceType, @Param("gender")String gender, @Param("firstResult") Integer firstResult,  @Param("pageSize") Integer pageSize);
	Integer getAllUserCount(@Param("userId")String userId, @Param("name")String name, @Param("deviceType")String deviceType, @Param("gender")String gender);
}