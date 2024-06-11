# 스프링 게시판

### 사용한 기술

- Mysql로 데이터베이스에 접속
- MVC 구조를 따름
- DAO와 DTO를 활용하여 DB관련 연동 작업 수행
- HttpSession을 통해 사용자에 대한 정보를 저장
- Pagination 클래스를 생성하여 게시물 페이징
- Spring-Security로 비밀번호 암호화 
- webfilter로 xss ,csrf 방어
- 중복로그인 방지
- 세션을 통해 게시글 인증 수정 삭제 권한 설정

### 데이터베이스

- 사용한 데이터베이스 : Mysql

- 테이블

-- 회원 테이블
<pre>
<code>
CREATE TABLE tbl_user (
    user_number INT AUTO_INCREMENT PRIMARY KEY,  
    user_id VARCHAR(50) NOT NULL UNIQUE,           
    user_pw VARCHAR(255) NOT NULL,           
    user_name VARCHAR(100) NOT NULL,        
    reg_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,              
    login_fail_count INT DEFAULT 0,          
    lock_until TIMESTAMP NULL DEFAULT NULL
);


</code>
</pre>

-- 게시판 테이블 생성
<pre>
<code>
CREATE TABLE tbl_board (
    bno INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    writer VARCHAR(100),
    regDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    viewCnt INT DEFAULT 0
);
</code>
</pre>


### 기능

- 게시글 목록 조회
- 게시글 목록 페이징
- 게시글 상세 정보 조회
- 게시글 업데이트
- 게시글 삭제
- 게시글 검색
- 게시글 조회수 증가
- 로그인, 로그아웃, 회원가입
