package com.jy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jy.dao.DeviceDao;
import com.jy.domain.Device;
import com.jy.exception.InvalidAttributesException;
import com.jy.utils.StringUtils;

public class DeviceServiceImpl implements DeviceService{

	@Autowired
	private DeviceDao deviceDao;
	
	
	@Override
	public void registerDevice(String userId, String udid)
			throws InvalidAttributesException {
		try {
			if(!StringUtils.hasLength(userId)){
				throw new InvalidAttributesException("Invalid parameter - userId"); 
			}
			if(!StringUtils.hasLength(udid)){
				throw new InvalidAttributesException("Invalid parameter - udid"); 
			}
			Device device = new Device();
			device.setUserId(userId);
			device.setUdid(udid);
			deviceDao.create(device);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}

	@Override
	public void disableDevice(String userId) throws InvalidAttributesException {
		try {
			if(!StringUtils.hasLength(userId)){
				throw new InvalidAttributesException("Invalid parameter - userId"); 
			}
			Device device = deviceDao.getUserDevice(userId);
			device.setIsDisable(true);
			deviceDao.update(device);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}

	@Override
	public List<Device> getBlackList() {
		try {
			return deviceDao.getBlackList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}

}
