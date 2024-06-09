package com.spring_tutorial.board.service;

import javax.servlet.http.HttpSession;

import com.spring_tutorial.board.error.*;
import com.spring_tutorial.board.model.dao.UserMapper;
import com.spring_tutorial.board.model.dto.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spring_tutorial.board.model.dao.MemberDaoImpl;
import com.spring_tutorial.board.model.dto.MemberDto;

import java.sql.Timestamp;


@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberDaoImpl memberDao;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void login(UserDto dto, HttpSession session)
			throws MemberNotFoundException, InvalidPasswordException, UserLockedException {
		String userId = dto.getUserId();
		String userPw = dto.getUserPw();

		UserDto user = userMapper.getUserByUserId(userId);

		if (user == null) {
			throw new InvalidPasswordException("사용자를 찾을 수 없습니다.");
		}

		// Check if the user is locked
		if (user.getLockUntil() != null && user.getLockUntil().after(new Timestamp(System.currentTimeMillis()))) {
			throw new UserLockedException("계정이 잠겨 있습니다. 잠금 해제 시간: " + user.getLockUntil());
		}

		// Check if the password matches
		if (!passwordEncoder.matches(userPw, user.getUserPw())) {
			// Increment login fail count
			userMapper.incrementLoginFailCount(userId);

			// If fail count reaches 5, lock the user
			if (user.getLoginFailCount() + 1 >= 5) {
				Timestamp lockUntil = new Timestamp(System.currentTimeMillis() + 15 * 60 * 1000); // 15 minutes lock
				userMapper.lockUser(userId, lockUntil);
			}

			throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
		}

		// Reset login fail count on successful login
		userMapper.resetLoginFailCount(userId);

		// Get user's name and set session attributes
		String name = userMapper.getUserName(userId);
		session.setAttribute("userId", userId);
		session.setAttribute("userName", name);
		generateCSRFToken(session);
	}

	private String generateCSRFToken(HttpSession session) {
		// CSRF 토큰 생성 로직
		String csrfToken = java.util.UUID.randomUUID().toString();
		session.setAttribute("csrfToken", csrfToken);
		return csrfToken;
	}




	@Override
	public void logout(HttpSession session) {
		session.invalidate();
	}

	@Override
	public void signup(MemberDto dto) {
		if (isUserIdExists(dto.getUserId())) {
			System.out.println(dto.getUserId());
			throw new IdAlreadyExistsException("이미 존재하는 아이디입니다");	}
		else{
			memberDao.signup(dto);
		}
	}
	@Override
	public String pwConfirmCheck(String userPw, String confirmPw) {
		if(!userPw.contentEquals(confirmPw)) throw new ConfirmPwDismatchException("비밀번호가 불일치합니다");
		return passwordEncoder.encode(userPw);
	}
	
	@Override
	public boolean pwDecodeCheck(MemberDto dto) {
		String rawPassword = dto.getUserPw();
		String encodedPassword = memberDao.getEncodedPassword(dto.getUserId());
		if(encodedPassword == null) throw new MemberNotFoundException("존재하지 않는 회원입니다");
		boolean result = passwordEncoder.matches(rawPassword, encodedPassword);
		if(!result) throw new InvalidPasswordException("비밀번호가 틀렸습니다");
		return result;
	}

	private boolean isUserIdExists(String userId) {
		return memberDao.countByUserId(userId);
	}

	
}
