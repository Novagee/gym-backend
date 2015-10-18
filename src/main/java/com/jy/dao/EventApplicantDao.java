package com.jy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jy.dataaccess.dao.GenericDao;
import com.jy.domain.EventApplicant;

public interface EventApplicantDao extends GenericDao<EventApplicant>{
	List<EventApplicant> getByEventId(@Param("eventId") Long eventId);
	List<EventApplicant> getByMyAppliedEvents(@Param("userId") String userId);
}