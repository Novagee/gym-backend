package com.jy.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jy.domain.Event;
import com.jy.domain.EventApplicant;
import com.jy.exception.InvalidAttributesException;


@Service
public interface EventService {
	
	public void createEvent(String title, String description, String starttime, String address, String fee, String pplCount, MultipartFile file) throws InvalidAttributesException;
	public void applyForEvent(Long eventId, String userId, String mobile) throws InvalidAttributesException;
	
	public List<Event> getAllEvents();
	public List<EventApplicant> getByEventId(@Param("eventId") Long eventId);
	public List<EventApplicant> getByMyAppliedEvents(@Param("userId") String userId);
	
}
