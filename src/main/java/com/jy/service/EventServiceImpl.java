package com.jy.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jy.dao.EventApplicantDao;
import com.jy.dao.EventDao;
import com.jy.domain.Event;
import com.jy.domain.EventApplicant;
import com.jy.exception.InvalidAttributesException;
import com.jy.utils.StringUtils;
import com.jy.utils.UploadUtils;
import com.jy.web.utils.ImageScale;

@Service
public class EventServiceImpl implements EventService{
	@Autowired
	private EventDao eventDao;
	
	@Autowired
	private EventApplicantDao eventApplicantDao;
	
	@Override
	public void createEvent(String title, String description, Long starttime,
			Long endtime, MultipartFile file) throws InvalidAttributesException {
		try {
			if(!StringUtils.hasLength(title)){
				throw new InvalidAttributesException("Invalid parameter - title"); 
			}
			if(!StringUtils.hasLength(description)){
				throw new InvalidAttributesException("Invalid parameter - description"); 
			}
			Event event = new Event();
			event.setTitle(title);
			event.setDescription(description);
			event.setIsActive(true);
//			if(starttime>0){
//				event.setStarttime(new Date(starttime));
//			}
//			if(endtime>0){
//				event.setEndtime(new Date(endtime));
//			}
			String fileName = "";
			if (null != file) {
				fileName = file.getOriginalFilename();
				if (StringUtils.hasLength(fileName)) {
					String finalFileName= UploadUtils.INSTANCE.getUploadFileName(UUID.randomUUID().toString(), fileName);
					ImageScale.scaleImage(file.getInputStream(), UploadUtils.INSTANCE.getUploadMockFilePath() + finalFileName);
					String finalFileHttpPath=UploadUtils.INSTANCE.getUploadMockHttpPath()+ finalFileName;
					event.setPic(finalFileHttpPath);
				}
			}
			eventDao.create(event);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}

	@Override
	public void applyForEvent(Long eventId, Long userId, String mobile)
			throws InvalidAttributesException {
		try {
			if(null == eventId){
				throw new InvalidAttributesException("Invalid parameter - eventId"); 
			}
			if(null == userId){
				throw new InvalidAttributesException("Invalid parameter - userId"); 
			}
			if(!StringUtils.hasLength(mobile)){
				throw new InvalidAttributesException("Invalid parameter - mobile"); 
			}
			EventApplicant eventApplicant = new EventApplicant();
			eventApplicant.setCreatetime(new Date());
			eventApplicant.setUserId(userId);
			eventApplicant.setEventId(eventId);
			eventApplicant.setMobile(mobile);
			eventApplicantDao.create(eventApplicant);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}

	@Override
	public List<Event> getAllEvents() {		
		return eventDao.getAllEvents();
	}

	@Override
	public List<EventApplicant> getByEventId(Long eventId) {
		if(eventId == null){
			throw new InvalidAttributesException("Invalid parameter - eventId"); 
		}
		return eventApplicantDao.getByEventId(eventId);
	}

	@Override
	public List<EventApplicant> getByMyAppliedEvents(Long userId) {
		// TODO Auto-generated method stub
		if(userId == null){
			throw new InvalidAttributesException("Invalid parameter - userId"); 
		}
		return eventApplicantDao.getByMyAppliedEvents(userId);
	}

	
}
