package com.spring_tutorial.board.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.spring_tutorial.board.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring_tutorial.board.model.dto.BoardDto;
import com.spring_tutorial.board.service.BoardPager;
import com.spring_tutorial.board.service.BoardServiceImpl;

import com.spring_tutorial.board.error.AuthorizationFailedException;
import com.spring_tutorial.board.error.InvalidMemberResourceException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/board/*")
public class BoardController {
    @Autowired
    private BoardService boardService;
    @RequestMapping(value = "/list.do")
    public ModelAndView list(
            @RequestParam(defaultValue = "1") int curPage,
            @RequestParam(defaultValue = "title") String searchOption,
            @RequestParam(defaultValue = "") String keyword,
            Model model) {

        // 검색어가 빈 문자열인 경우 null로 처리
        if (keyword.isEmpty()) {
            keyword = null;
        }

        // 게시글 검색 결과를 담을 Map
        Map<String, Object> map = new HashMap<>();

        // 검색 조건에 맞는 결과를 조회
        try {
            map = boardService.listAll(curPage, searchOption, keyword);
        } catch (IndexOutOfBoundsException e) {
            // 검색 결과가 없는 경우 예외 처리
            map.put("error", "No results found for the given search criteria.");
        }

        // 게시물 개수
        Integer countObject = (Integer) map.get("count");
        int count = (countObject != null) ? countObject.intValue() : 0;

        ModelAndView mav = new ModelAndView();
        mav.addObject("map", map);

        // 게시물 개수에 따라 메시지 설정
        if (count == 0) {
            mav.addObject("message", "게시물이 없습니다.");
        } else {
            mav.addObject("message", count + "개의 게시물이 있습니다.");
        }

        mav.setViewName("board/list");
        return mav;
    }

    @RequestMapping(value = "/write_view.do", method = RequestMethod.GET)
    public String writevalue(BoardDto dto) {
        return "board/write";
    }

    @RequestMapping(value = "/detail.do", method = RequestMethod.GET)
    public ModelAndView view(@RequestParam("bno") int bno, Model model) {
        // 조회수 증가
        boardService.updateViews(bno);

        // 게시물 상세 내용 조회 (가정: 상세 내용 조회 메서드가 있다고 가정)
        BoardDto boardDto = boardService.detail(bno);

        ModelAndView mav = new ModelAndView();
        mav.addObject("dto", boardDto);
        mav.setViewName("board/detail");
        return mav;
    }

    @RequestMapping(value = "/update_view.do", method = RequestMethod.GET)
    public ModelAndView viewer(@RequestParam("boardId") int bno, HttpSession session) {
        // 게시물 상세 내용 조회
        BoardDto boardDto = boardService.detail(bno);
        // 현재 로그인한 사용자의 아이디 가져오기
        String loggedInUserId = (String) session.getAttribute("userId");

        // 작성자와 현재 로그인한 사용자의 아이디 비교
        if (!boardDto.getWriter().equals(loggedInUserId)) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("redirect:/board/list.do");
            mav.addObject("msg", "unauthorized");
            return mav;
        }

        // 작성자가 일치할 경우 수정 페이지로 이동
        ModelAndView mav = new ModelAndView();
        mav.addObject("dto", boardDto);
        mav.setViewName("board/update");
        return mav;
    }
    private String generateCSRFToken(HttpSession session) {
        // CSRF 토큰 생성 로직
        String csrfToken = java.util.UUID.randomUUID().toString();
        session.setAttribute("csrfToken", csrfToken);
        return csrfToken;
    }

    @RequestMapping(value = "/write.do", method = RequestMethod.POST)
    public String write(BoardDto dto, HttpSession session, @RequestParam("csrfToken") String requestToken) {
        // CSRF 토큰 검증
        String sessionToken = (String) session.getAttribute("csrfToken");
        if (sessionToken == null || !sessionToken.equals(requestToken)) {
            // 검증 실패: 에러 페이지 표시 또는 리다이렉트
            return "error";
        }

        // 검증 성공: 비즈니스 로직 수행
        boardService.create(dto);
        generateCSRFToken(session);
        return "redirect:/board/list.do";
    }

    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    public String update(BoardDto dto, HttpSession session, RedirectAttributes redirectAttributes, @RequestParam("csrfToken") String requestToken) {
        // CSRF 토큰 검증
        String sessionToken = (String) session.getAttribute("csrfToken");
        if (sessionToken == null || !sessionToken.equals(requestToken)) {
            // 검증 실패: 에러 페이지 표시 또는 리다이렉트
            return "error";
        }

        // 현재 로그인한 사용자의 아이디 가져오기
        String loggedInUserId = (String) session.getAttribute("userId");

        // 게시물의 작성자 아이디와 비교
        BoardDto existingBoardDto = boardService.detail(dto.getBno());
        if (!existingBoardDto.getWriter().equals(loggedInUserId)) {
            redirectAttributes.addFlashAttribute("msg", "unauthorized");
            return "redirect:/board/list.do";
        }

        // 작성자가 일치할 경우 업데이트 진행
        boardService.update(dto);
        generateCSRFToken(session);

        return "redirect:/board/list.do";
    }

    @RequestMapping(value = "/delete.do", method = RequestMethod.POST)
    public String delete(@RequestParam("boardId") int bno, HttpSession session, RedirectAttributes redirectAttributes, @RequestParam("csrfToken") String requestToken) {
        // CSRF 토큰 검증
        String sessionToken = (String) session.getAttribute("csrfToken");
        if (sessionToken == null || !sessionToken.equals(requestToken)) {
            // 검증 실패: 에러 페이지 표시 또는 리다이렉트
            return "error";
        }

        // 현재 로그인한 사용자의 아이디 가져오기
        String loggedInUserId = (String) session.getAttribute("userId");

        // 게시물의 작성자 아이디와 비교
        BoardDto existingBoardDto = boardService.detail(bno);
        if (!existingBoardDto.getWriter().equals(loggedInUserId)) {
            redirectAttributes.addFlashAttribute("msg", "unauthorized");
            return "redirect:/board/list.do";
        }

        // 작성자가 일치할 경우 삭제 진행
        boardService.delete(bno);
        generateCSRFToken(session);

        return "redirect:/board/list.do";
    }
}