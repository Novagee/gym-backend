package com.jy.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.jy.domain.Device;
import com.jy.exception.InvalidAttributesException;

@Service
public interface DeviceService {
	public void registerDevice(String userId, String udid) throws InvalidAttributesException;
	public void disableDevice(String userId) throws InvalidAttributesException;
	public List<Device> getBlackList();
}
