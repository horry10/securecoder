package com.spring_tutorial.board.service;

import java.util.List;
import java.util.Map;

import com.spring_tutorial.board.model.dto.BoardDto;


public interface BoardService {
    Map<String, Object> listAll(int curPage, String searchOption, String keyword);

    void updateViews(int bno);

    void create(BoardDto dto);
    BoardDto detail(int bno);
    void update(BoardDto dto);
    void delete(int bno);


}
