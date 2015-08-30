package com.jy.web.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jy.domain.Complaint;
import com.jy.domain.Event;
import com.jy.domain.EventApplicant;
import com.jy.domain.User;
import com.jy.domain.rest.EventVO;
import com.jy.exception.InvalidAttributesException;
import com.jy.service.ComplaintService;
import com.jy.service.EventService;
import com.jy.utils.DateUtils;
import com.jy.utils.JsonResponse;
import com.jy.utils.JsonResponseACK;
import com.jy.utils.JsonResponseWithError;
import com.jy.utils.JsonResponseWithObj;
import com.jy.utils.StringUtils;
import com.jy.utils.UploadUtils;
import com.jy.web.validator.UserFormValidator;


@RequestMapping("/r/complaint")
@Controller
public class ComplaintController{
	
	@Autowired
	private ComplaintService complaintService;

	private Logger logger = Logger.getLogger(ComplaintController.class);
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public @ResponseBody JsonResponse getComplaints(HttpServletRequest request) {
		try {
			List<Complaint> complaints = complaintService.getComplaints();
			return new JsonResponseWithObj(JsonResponseACK.Success.name(), DateUtils.switchNowToString(),complaints);
		} catch (InvalidAttributesException e) {
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), "Invalid userId");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), "Unknown error");
		}
	}
	
	@RequestMapping(value="/enable", method = RequestMethod.GET)
	public @ResponseBody JsonResponse getEnable(HttpServletRequest request) {
		try {
			String enabled = UploadUtils.INSTANCE.getComplaintEnable();
			return new JsonResponseWithObj(JsonResponseACK.Success.name(), DateUtils.switchNowToString(),enabled);
		} catch (InvalidAttributesException e) {
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), "Invalid userId");
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
			complaintService.createComplaint(userId);
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
