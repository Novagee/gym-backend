package com.jy.domain.rest;

import java.util.List;

import com.jy.domain.Event;
import com.jy.domain.EventApplicant;

public class EventVO extends Event{
	private Boolean hasApplied;
	private List<EventApplicant> applicants;

	public List<EventApplicant> getApplicants() {
		return applicants;
	}

	public void setApplicants(List<EventApplicant> applicants) {
		this.applicants = applicants;
	}

	public Boolean getHasApplied() {
		return hasApplied;
	}

	public void setHasApplied(Boolean hasApplied) {
		this.hasApplied = hasApplied;
	}
	
	
}
