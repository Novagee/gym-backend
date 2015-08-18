package com.jy.dao;

import org.apache.ibatis.annotations.Param;

import com.jy.dataaccess.dao.GenericDao;
import com.jy.domain.UserBlock;

public interface UserBlockDao extends GenericDao<UserBlock>{
	UserBlock getByUserIdAndBlockedUser(@Param("userId")Long userId, @Param("userBlocked")Long userBlocked);
}