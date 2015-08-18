package com.jy.domain;

import java.util.Date;

import com.jy.dataaccess.dao.AbstractDomainObject;

public class UserBlock extends AbstractDomainObject{
    private Long id;

    private Long userId;

    private Long userBlocked;

    private Date createtime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserBlocked() {
        return userBlocked;
    }

    public void setUserBlocked(Long userBlocked) {
        this.userBlocked = userBlocked;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}