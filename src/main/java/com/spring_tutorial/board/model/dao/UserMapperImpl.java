package com.spring_tutorial.board.model.dao;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.spring_tutorial.board.model.dto.UserDto;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring_tutorial.board.model.dto.UserDto;
import java.sql.Timestamp;
import java.util.Map;
import java.util.HashMap;

@Repository
public class UserMapperImpl implements UserMapper {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public UserDto getUserByUserId(String userId) {
        return sqlSession.selectOne("usersign.getUserByUserId", userId);
    }

    @Override
    public void incrementLoginFailCount(String userId) {
        sqlSession.update("usersign.incrementLoginFailCount", userId);
    }

    @Override
    public void resetLoginFailCount(String userId) {
        sqlSession.update("usersign.resetLoginFailCount", userId);
    }

    @Override
    public void lockUser(String userId, Timestamp lockUntil) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("lockUntil", lockUntil);

        sqlSession.update("usersign.lockUser", map);
    }

    @Override
    public String getUserName(String userId) {
        return sqlSession.selectOne("usersign.getUserName", userId);
    }
}
