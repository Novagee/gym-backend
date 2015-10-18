package com.jy.web.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jy.domain.Device;
import com.jy.exception.InvalidAttributesException;
import com.jy.service.DeviceService;
import com.jy.utils.DateUtils;
import com.jy.utils.JsonResponse;
import com.jy.utils.JsonResponseACK;
import com.jy.utils.JsonResponseWithError;
import com.jy.utils.JsonResponseWithObj;
import com.jy.web.validator.UserFormValidator;


@RequestMapping("/r/device")
@Controller
public class DeviceController{
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private UserFormValidator userFormValidator;
	
	private Logger logger = Logger.getLogger(DeviceController.class);
	
	@RequestMapping(value="/blacklist", method = RequestMethod.GET)
	public @ResponseBody JsonResponse getEvents(HttpServletRequest request) {
		try {
			List<Device> devices = deviceService.getBlackList();
			return new JsonResponseWithObj(JsonResponseACK.Success.name(), DateUtils.switchNowToString(),devices);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), "Unknown error");
		}
	}
	
	@RequestMapping(value="/create", method = RequestMethod.POST)
	public @ResponseBody JsonResponse createEvent(HttpServletRequest request) {
		try {
			String userId = request.getParameter("userId");
			String udid = request.getParameter("udid");
			deviceService.registerDevice(userId, udid);
			return new JsonResponse(JsonResponseACK.Success.name(), DateUtils.switchNowToString());
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), "Invalid paramer");
		} catch (InvalidAttributesException e) {
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), "Unknown error");
		}
	}
	
	@RequestMapping(value="/disable", method = RequestMethod.POST)
	public @ResponseBody JsonResponse disableDevice(HttpServletRequest request) {
		try {
			String userId = request.getParameter("userId");
			deviceService.disableDevice(userId);
			return new JsonResponse(JsonResponseACK.Success.name(), DateUtils.switchNowToString());
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), "Invalid paramer");
		} catch (InvalidAttributesException e) {
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), "Unknown error");
		}
	}
	
}
