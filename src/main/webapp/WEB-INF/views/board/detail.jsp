<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${dto.title}</title>
    <%@ include file="../include/header.jsp" %>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {
            $("#btnUpdate").click(function() {
                location.href = "${pageContext.request.contextPath}/board/update_view.do?boardId=${dto.bno}";
            });

            $("#btnDelete").click(function() {
                if (confirm("정말로 삭제하시겠습니까?")) {
                    // CSRF 토큰 가져오기
                    var csrfToken = "${sessionScope.csrfToken}";

                    // POST 요청으로 변환
                    $.post("${pageContext.request.contextPath}/board/delete.do", { boardId: ${dto.bno}, csrfToken: csrfToken })
                        .done(function() {
                            alert("삭제되었습니다.");
                            location.href = "${pageContext.request.contextPath}/board/list.do?curPage=${param.curPage}&searchOption=${param.searchOption}&keyword=${param.keyword}";
                        })
                        .fail(function() {
                            alert("삭제에 실패하였습니다.");
                        });
                }
            });

            $("#btnList").click(function() {
                // 현재 페이지, 검색 옵션, 검색 키워드를 유지한채 목록으로 이동
                location.href = "${pageContext.request.contextPath}/board/list.do?curPage=${param.curPage}&searchOption=${param.searchOption}&keyword=${param.keyword}";
            });
        });
    </script>
</head>
<body>
    <%@ include file="../include/nav.jsp" %>
    <h2>${dto.title}</h2>
    <div>
        작성일 :
        <fmt:formatDate value="${dto.regDate}" pattern="yyyy-MM-dd HH:mm:ss" />
    </div>
    <div>
        조회수 :
        ${dto.viewCnt}
    </div>
    <div>
        작성자 :
        ${dto.writer}
    </div>
    <div>
        내용 :
        ${dto.content}
    </div>
    <c:if test="${sessionScope.userId == dto.writer}">
        <button id="btnUpdate">수정</button>
        <button id="btnDelete">삭제</button>
        <input type="hidden" id="csrfToken" value="${sessionScope.csrfToken}">
    </c:if>
    <button id="btnList">목록</button>
</body>
</html>
