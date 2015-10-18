package com.jy.web.rest;

import java.util.ArrayList;
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

import com.jy.domain.Event;
import com.jy.domain.EventApplicant;
import com.jy.domain.rest.EventVO;
import com.jy.exception.InvalidAttributesException;
import com.jy.service.EventService;
import com.jy.utils.DateUtils;
import com.jy.utils.JsonResponse;
import com.jy.utils.JsonResponseACK;
import com.jy.utils.JsonResponseWithError;
import com.jy.utils.JsonResponseWithObj;
import com.jy.utils.StringUtils;
import com.jy.web.validator.UserFormValidator;


@RequestMapping("/r/event")
@Controller
public class EventController{
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UserFormValidator userFormValidator;
	
	private Logger logger = Logger.getLogger(EventController.class);
	
	@RequestMapping(value="/", method = RequestMethod.GET)
	public @ResponseBody JsonResponse getEvents(HttpServletRequest request) {
		try {
			List<Event> events = eventService.getAllEvents();
			if(StringUtils.hasLength(request.getParameter("userId"))){
				String userId = request.getParameter("userId");
				List<EventVO> eventList = new ArrayList<EventVO>();
				List<EventApplicant> eas = eventService.getByMyAppliedEvents(userId);
				for (Event event : events) {
					EventVO vo = new EventVO();
					vo.setId(event.getId());
					vo.setTitle(event.getTitle());
					vo.setDescription(event.getDescription());
					vo.setStarttime(event.getStarttime());
					vo.setEndtime(event.getEndtime());
					vo.setIsActive(event.getIsActive());
					vo.setPic(event.getPic());
					vo.setHasApplied(false);
					for(EventApplicant ea : eas){
						if(ea.getEventId() == vo.getId()){
							vo.setHasApplied(true);
							break;
						}
					}
					eventList.add(vo);
				}
				return new JsonResponseWithObj(JsonResponseACK.Success.name(), DateUtils.switchNowToString(),eventList);
			}else{
				return new JsonResponseWithObj(JsonResponseACK.Success.name(), DateUtils.switchNowToString(),events);
			}
		} catch (NumberFormatException e) {
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), "Invalid userId");
		} catch (InvalidAttributesException e) {
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), "Unknown error");
		}
	}
	
	@RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
	public @ResponseBody JsonResponse getEventApplicants(@PathVariable("eventId") Long eventId, HttpServletRequest request) {
		try {
			List<EventApplicant> eventApplicants = eventService.getByEventId(eventId);
			return new JsonResponseWithObj(JsonResponseACK.Success.name(), DateUtils.switchNowToString(),eventApplicants);
		} catch (InvalidAttributesException e) {
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return new JsonResponseWithError(JsonResponseACK.Failure.name(), DateUtils.switchNowToString(), "Unknown error");
		}
	}
	
	@RequestMapping(value="/create", method = RequestMethod.POST)
	public @ResponseBody JsonResponse createEvent(HttpServletRequest request) {
		try {
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			String starttime = request.getParameter("starttime");
			String address = request.getParameter("address");
			String fee = request.getParameter("fee");
			String pplCount = request.getParameter("pplCount");
			if (starttime.indexOf(".")!=-1) {
				starttime = starttime.substring(0, starttime.indexOf("."));
			}
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Map<?, MultipartFile> files = multiRequest.getFileMap();
			MultipartFile file = null;
			if (null != files) {
				for (MultipartFile f : files.values()) {
					file = f;
					break;
				}
			}
			eventService.createEvent(title, description, Long.parseLong(starttime),address, fee, pplCount, file);
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
	
	@RequestMapping(value="/{eventId}/apply", method = RequestMethod.POST)
	public @ResponseBody JsonResponse applyForEvent(@PathVariable("eventId") Long eventId, HttpServletRequest request) {
		try {
//			String uuid = request.getParameter("uuid");
			String mobile = request.getParameter("mobile");
//			User user = userFormValidator.validateSecurity(uuid);
			eventService.applyForEvent(eventId, "" , mobile);
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
