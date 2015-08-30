package com.jy.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jy.dao.ComplaintDao;
import com.jy.domain.Complaint;
import com.jy.exception.InvalidAttributesException;
import com.jy.utils.StringUtils;

@Service
public class ComplaintServiceImpl implements ComplaintService{
	@Autowired
	private ComplaintDao complaintDao;
	
	@Override
	public void createComplaint(String userId) throws InvalidAttributesException {
		try {
			if(!StringUtils.hasLength(userId)){
				throw new InvalidAttributesException("Invalid parameter - userId"); 
			}
			Complaint complaint = new Complaint();
			complaint.setUserId(userId);
			complaint.setCreatetime(new Date());
			complaintDao.create(complaint);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidAttributesException(e.getMessage());
		}
	}

	@Override
	public List<Complaint> getComplaints() {		
		return complaintDao.getComplaints();
	}

	
}
