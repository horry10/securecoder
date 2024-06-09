package com.spring_tutorial.board.model.dao;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring_tutorial.board.model.dto.BoardDto;

@Repository
public class BoardDaoImpl implements BoardDao {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public int countArticles(String searchOption, String keyword) {
		Map<String, String> map = new HashMap<>();
		map.put("searchOption", searchOption);
		map.put("keyword", keyword);
		return sqlSession.selectOne("board.countArticle", map);
	}

	@Override
	public List<BoardDto> listAll(int start, int end, String searchOption, String keyword) {
		Map<String, Object> map = new HashMap<>();
		map.put("start", start);
		map.put("end", end);
		map.put("searchOption", searchOption);
		map.put("keyword", keyword);
		return sqlSession.selectList("board.listAll", map);
	}

	@Override
	public void updateViews(int bno) {
		sqlSession.update("board.updateViews", bno);
	}

	@Override
	public void create(BoardDto dto) {
		sqlSession.insert("board.create", dto);
	}

	@Override
	public BoardDto detail(int bno) {
		return sqlSession.selectOne("board.detail", bno);
	}

	@Override
	public void update(BoardDto dto) {
		sqlSession.update("board.update", dto);
	}

	@Override
	public void delete(int bno) {
		sqlSession.delete("board.delete", bno);
	}
	

}
