package com.jy.domain;

import java.util.Date;

import com.jy.dataaccess.dao.AbstractDomainObject;

public class Device extends AbstractDomainObject{
    private Long id;
    private String userId;
    private String udid;
    private Date createtime;
    private Boolean isDisable;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUdid() {
		return udid;
	}
	public void setUdid(String udid) {
		this.udid = udid;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Boolean getIsDisable() {
		return isDisable;
	}
	public void setIsDisable(Boolean isDisable) {
		this.isDisable = isDisable;
	} 
	
	

}