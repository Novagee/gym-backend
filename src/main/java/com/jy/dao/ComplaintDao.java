package com.jy.dao;

import java.util.List;

import com.jy.dataaccess.dao.GenericDao;
import com.jy.domain.Complaint;

public interface ComplaintDao extends GenericDao<Complaint>{
	List<Complaint> getComplaints();
}