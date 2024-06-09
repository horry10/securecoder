package com.spring_tutorial.board.model.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.spring_tutorial.board.model.dto.UserDto;

import java.sql.Timestamp;

@Mapper
public interface UserMapper {
    UserDto getUserByUserId(String userId);

    void incrementLoginFailCount(String userId);

    void resetLoginFailCount(String userId);

    void lockUser(@Param("userId") String userId, @Param("lockUntil") Timestamp lockUntil);

    String getUserName(String userId);
}
