# 스프링 게시판

### 사용한 기술

- Mybatis로 데이터베이스에 접속
- MVC 구조를 따름
  1. 클라이언트 요청이 들어온 후 컨트롤러가 해당 요청 함수로 매핑
  2. 요청 함수에서 Service(비즈니스 로직) 함수를 호출
  3. ModelAndView 객체를 생성하여 뷰와 로직 데이터를 저장
  4. 해당 뷰로 렌더링
- 비즈니스 로직들은 인터페이스를 활용하여 유지보수와 효율성을 높임
- DAO와 DTO를 활용하여 DB관련 연동 작업 수행
- HttpSession을 통해 사용자에 대한 정보를 저장
- Pagination 클래스를 생성하여 게시물 페이징
- Spring-Security로 비밀번호 암호화

### 데이터베이스

- 액세스 방법 : Mybatis
- 사용한 데이터베이스 : MariaDB

- 테이블

-- 회원 테이블
<pre>
<code>
CREATE TABLE tbl_user (
  userid VARCHAR(50) NOT NULL,
  userpw VARCHAR(50) NOT NULL,
  username VARCHAR(100) NOT NULL,
  regdate TIMESTAMP    NOT NULL DEFAULT NOW(),
  usable char(1)        DEFAULT 'Y',
  PRIMARY KEY (userid)
);
</code>
</pre>

-- 게시판 테이블 생성
<pre>
<code>
CREATE TABLE tbl_board (
  bno     INT          NOT NULL AUTO_INCREMENT,
  title   VARCHAR(200) NOT NULL,
  content TEXT         NOT NULL,
  writer  VARCHAR(50)  NOT NULL,
  regdate TIMESTAMP    NOT NULL DEFAULT NOW(),
  upddate TIMESTAMP    NOT NULL DEFAULT NOW(),
  viewcnt INT                   DEFAULT 0,
  usable char(1)        DEFAULT 'Y',
  PRIMARY KEY (bno)
);
</code>
</pre>

-- 첨부파일 테이블
<pre>
<code>
CREATE TABLE tbl_attach (
  ano     INT          NOT NULL AUTO_INCREMENT,
  bno INT NOT NULL,
  uploadpath VARCHAR(150) NOT NULL,
  filename VARCHAR(150) NOT NULL,
  filetype INT                   DEFAULT 0,
  regdate TIMESTAMP DEFAULT NOW(),
  PRIMARY KEY (fullname)
);
</code>
</pre>

-- 댓글 테이블
<pre>
<code>
CREATE TABLE tbl_reply (
  rno INT NOT NULL AUTO_INCREMENT,
  bno INT NOT NULL DEFAULT 0,
  replyid VARCHAR(50) NOT NULL,
  reply VARCHAR(1000) NOT NULL,
  regdate TIMESTAMP NOT NULL DEFAULT NOW(),
  upddate TIMESTAMP NOT NULL DEFAULT NOW(),
  usable char(1)        DEFAULT 'Y',
  PRIMARY KEY (rno)
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
