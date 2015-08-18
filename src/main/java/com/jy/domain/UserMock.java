package com.jy.domain;

import java.util.Date;

import com.jy.dataaccess.dao.AbstractDomainObject;
import com.jy.domain.rest.UserVO;

public class UserMock extends AbstractDomainObject{
	
    private Integer id;

    private UserVO userVO;
    
    private Long userId;
    
    private Date createTime;

    private Boolean enabled;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserVO getUserVO() {
		return userVO;
	}

	public void setUserVO(UserVO userVO) {
		this.userVO = userVO;
	}

	public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
    
    

}