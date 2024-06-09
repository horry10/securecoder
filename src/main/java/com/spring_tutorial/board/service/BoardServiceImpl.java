package com.spring_tutorial.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring_tutorial.board.model.dto.BoardDto;
import com.spring_tutorial.board.model.dao.BoardDaoImpl;


@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	BoardDaoImpl boardDao;


	@Override
	public Map<String, Object> listAll(int curPage, String searchOption, String keyword) {
		int count = boardDao.countArticles(searchOption, keyword); // 게시물 수
		System.out.println(count);
		BoardPager boardPager = new BoardPager(count, curPage);
		int start = boardPager.getPageBegin();
		System.out.println("시작"+start);

		int end = boardPager.getPageEnd();
		System.out.println("끝"+end);

		List<BoardDto> list = boardDao.listAll(start, end, searchOption, keyword);
		if (list == null || list.isEmpty()) {
			throw new IndexOutOfBoundsException("No results found for the given search criteria.");
		}
		System.out.println(list.get(0).getRownuma());

		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("count", count);
		map.put("searchOption", searchOption);
		map.put("keyword", keyword);
		map.put("boardPager", boardPager);
		return map;
	}

	@Override
	public void updateViews(int bno) {
		boardDao.updateViews(bno);
	}

	@Override
	public void create(BoardDto dto) {
		boardDao.create(dto);
	}

	@Override
	public BoardDto detail(int bno) {
		return boardDao.detail(bno);
	}

	@Override
	public void update(BoardDto dto) {
		boardDao.update(dto);
	}

	@Override
	public void delete(int bno) {
		boardDao.delete(bno);
	}




}
