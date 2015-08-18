package com.jy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jy.dataaccess.dao.GenericDao;
import com.jy.domain.UserMock;
import com.jy.domain.rest.MessageVO;

public interface UserMockDao extends GenericDao<UserMock>{
   List<UserMock> getAllMocks();
   List<MessageVO> getAllMockMessages();
   UserMock getMockByUserId(@Param("userId") Long userId);
}