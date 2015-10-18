package com.jy.domain;

import com.jy.dataaccess.dao.AbstractDomainObject;

public class Event extends AbstractDomainObject{
    private Long id;
    private String title;
    private String description;
    private	String address;
    private String fee;
    private String starttime;
    private String endtime;
    private Boolean isActive;
    private String pic;
    private String pplCount;
    
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getPplCount() {
		return pplCount;
	}
	public void setPplCount(String pplCount) {
		this.pplCount = pplCount;
	}
    
    
    
}