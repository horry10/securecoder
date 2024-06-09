<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 작성 페이지</title>
    <%@ include file="../include/header.jsp" %>
    <%@ include file="../include/sessionCheck.jsp" %>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {
            $("#btnSave").click(function() {
                const title = $("#title").val();
                const content = $("#content").val();
                const writer = "${sessionScope.userId == null ? '' : sessionScope.userId}";

                if (title === "") {
                    alert("제목을 입력하십시오.");
                    $("#title").focus();
                    return;
                }
                if (content === "") {
                    alert("내용을 입력하십시오.");
                    $("#content").focus();
                    return;
                }
                if (writer === "") {
                    alert("로그인 후에 작성할 수 있습니다.");
                    return;
                }
                // CSRF 토큰 가져오기
                const csrfToken = $("#csrfToken").val();
                // 폼에 CSRF 토큰 추가
                // 폼 제출

                document.write_form.submit();

            });
        });
        function goToBoardList() {
            location.href = "${pageContext.request.contextPath}/board/list.do";
        }
    </script>
</head>
<body>
    <%@ include file="../include/nav.jsp" %>
    <h2>게시글 작성</h2>

    <form name="write_form" method="post" action="${path}/board/write.do">
        <div>
            제목
            <input name="title" id="title" size="100" placeholder="제목을 입력하십시오" />
        </div>
        <div>
            내용
            <textarea name="content" id="content" rows="5" cols="100" placeholder="내용을 입력하십시오"></textarea>
        </div>
        <input type="hidden" id="writer" name="writer" value="${sessionScope.userId == null ? '' : sessionScope.userId}">
        <!-- CSRF 토큰 추가 -->
        <input type="hidden" id="csrfToken" name="csrfToken" value="${sessionScope.csrfToken}">

        <div style="width: 650px; text-align: center;">
            <button type="button" id="btnSave">확인</button>
            <button type="reset" onclick="goToBoardList()">취소</button>
        </div>
    </form>

    <!-- 첨부파일 -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">File Attach</div>
                <!-- /.pannel-heading -->
                <div class="panel-body">
                    <div class="form-group uploadDiv">
                        <input type="file" name ='uploadFile' multiple>
                    </div>

                    <div class='uploadResult'>
                    <ul></ul>
                    </div>
                </div>
                <!--  end panel-body -->
            </div>
            <!-- end panel -->
        </div>
        <!-- end col -->
    </div>
    <!-- end row -->

</body>
</html>
