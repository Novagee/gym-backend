package com.jy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jy.dataaccess.dao.GenericDao;
import com.jy.domain.Event;

public interface EventDao extends GenericDao<Event>{
	Event getById(@Param("id") Long id);
	List<Event> getAllEvents();
}