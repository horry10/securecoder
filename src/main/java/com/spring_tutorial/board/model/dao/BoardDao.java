package com.spring_tutorial.board.model.dao;

import java.util.List;

import com.spring_tutorial.board.model.dto.BoardDto;

public interface BoardDao {
    int countArticles(String searchOption, String keyword);
    List<BoardDto> listAll(int start, int end, String searchOption, String keyword);

    void updateViews(int bno);

    void create(BoardDto dto);
    BoardDto detail(int bno);
    void update(BoardDto dto);
    void delete(int bno);


	
}
