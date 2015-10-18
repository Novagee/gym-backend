package com.jy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jy.dataaccess.dao.GenericDao;
import com.jy.domain.Device;

public interface DeviceDao extends GenericDao<Device>{
	List<Device> getBlackList();
	Device getUserDevice(@Param("userId") String userId);
}