package com.jy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jy.domain.Complaint;
import com.jy.exception.InvalidAttributesException;


@Service
public interface ComplaintService {
	
	public void createComplaint(String userId) throws InvalidAttributesException;
	
	public List<Complaint> getComplaints();
	
}
