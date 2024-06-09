<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 업데이트</title>
    <%@ include file="../include/header.jsp" %>
    <%@ include file="../include/sessionCheck.jsp" %>
    <script>
        function submitForm() {
            const title = document.getElementById('title').value;
            const content = document.getElementById('content').value;
            const writer = "${sessionScope.userId == null ? '' : sessionScope.userId}";

            if (title === "") {
                alert("제목을 입력하십시오.");
                document.getElementById('title').focus();
                return;
            }
            if (content === "") {
                alert("내용을 입력하십시오.");
                document.getElementById('content').focus();
                return;
            }
            if (writer === "") {
                alert("로그인 후에 작성할 수 있습니다.");
                return;
            }
            // CSRF 토큰 가져오기
            var csrfToken = document.getElementById('csrfToken').value;
            // 폼 제출
            document.update_form.submit();

        }
             function goToBoardList() {
                         location.href = "${pageContext.request.contextPath}/board/list.do";
                    }
    </script>
</head>
<body>
    <%@ include file="../include/nav.jsp" %>
    <h2>게시글 업데이트</h2>
    <form name="update_form" method="post" action="${pageContext.request.contextPath}/board/update.do">
        <input name="bno" id="bno" value="${dto.getBno()}" type="hidden" />
        <div>
            제목 :
            <input name="title" id="title" size="100" value="${dto.getTitle()}" />
        </div>
        <div>
            내용 :
            <textarea name="content" id="content" rows="5" cols="100">${dto.getContent()}</textarea>
        </div>
        <div>
            작성자 : ${sessionScope.userId} <!-- 세션에서 writer 정보 가져오기 -->
            <input name="writer" id="writer" value="${sessionScope.userId}" type="hidden" />
            <input type="hidden" id="csrfToken" name="csrfToken" value="${sessionScope.csrfToken}">
        </div>
        <div style="width: 650px; text-align: center;">
            <button type="button" id="btnUpdate" onclick="submitForm()">확인</button>
            <button type="reset" onclick="goToBoardList()">취소</button>
        </div>
    </form>

    <c:if test="${msg eq 'unauthorized'}">
        <div style="color: red;">
            수정 권한이 없습니다.
        </div>
    </c:if>
</body>
</html>
