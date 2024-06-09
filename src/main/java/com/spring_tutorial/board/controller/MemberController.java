package com.spring_tutorial.board.controller;

import javax.servlet.http.HttpSession;

import com.spring_tutorial.board.error.ConfirmPwDismatchException;
import com.spring_tutorial.board.model.dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring_tutorial.board.model.dto.MemberDto;
import com.spring_tutorial.board.service.MemberServiceImpl;


@Controller
@RequestMapping("/member/*")
public class MemberController {
	
	@Autowired
	MemberServiceImpl memberService;
	
	@RequestMapping("login_view.do")
	public String loginView() {
		return "member/login";
	}
	
	@RequestMapping("login.do")
	public ModelAndView login(@ModelAttribute UserDto dto, HttpSession session) {
		memberService.login(dto, session);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("main");
		mav.addObject("msg", "loginSuccess");
		return mav;
	}
	
	@RequestMapping("logout.do")
	public ModelAndView logout(HttpSession session) {
		memberService.logout(session);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("main");
		return mav;
	}
	
	@RequestMapping("signup_view.do")
	public String signupView() {
		return "member/signup";
	}

	@RequestMapping(value="signup.do", method=RequestMethod.POST)
	public ModelAndView signup(@ModelAttribute MemberDto dto, @RequestParam("confirmPw") String confirmPw) {
		ModelAndView mav = new ModelAndView();

		// 비밀번호 확인
		String encodedPassword = memberService.pwConfirmCheck(dto.getUserPw(), confirmPw);

		// 비밀번호 규칙 적용
		if (!isPasswordValid(dto.getUserPw())) {
			mav.setViewName("member/signup"); // 비밀번호 규칙을 충족하지 않으면 회원가입 페이지로 다시 이동
			mav.addObject("msg", "invalidPassword");
			return mav;
		}

		// 암호화된 비밀번호를 dto에 설정
		dto.setUserPw(encodedPassword);

		// 회원가입 처리
		memberService.signup(dto);
		mav.setViewName("member/login");
		mav.addObject("msg", "signupSuccess");

		return mav;
	}

	// 비밀번호 규칙을 확인하는 메서드
	private boolean isPasswordValid(String password) {
		// 비밀번호는 최소 8자 이상, 최소 1개의 숫자, 최소 1개의 대문자, 최소 1개의 소문자를 포함해야 함
		String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
		return password.matches(regex);
	}

}
