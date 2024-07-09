<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세 페이지</title>
<style>
    body {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
        font-family: Arial, sans-serif;
    }
    .container {
        width: 80%;
        max-width: 800px;
        border: 1px solid #ccc;
        padding: 20px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        border-radius: 10px;
        background-color: #f9f9f9;
    }
    h2 {
        text-align: center;
        margin-bottom: 20px;
    }
    table {
        width: 100%;
        border-collapse: collapse;
    }
    table, th, td {
        border: 1px solid #ccc;
    }
    th, td {
        padding: 10px;
        text-align: left;
    }
    th {
        background-color: #007bff;
        color: #fff;
    }
    .form-group {
        margin-top: 20px;
        text-align: right;
    }
    .form-group input[type="button"] {
        padding: 10px 20px;
        font-size: 16px;
        color: #fff;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        margin-right: 10px;
    }
    .btn-blue {
        background-color: #007bff;
    }
    .btn-blue:hover {
        background-color: #0056b3;
    }
    .btn-green {
        background-color: #28a745;
    }
    .btn-green:hover {
        background-color: #218838;
    }
    .btn-red {
        background-color: #dc3545;
    }
    .btn-red:hover {
        background-color: #c82333;
    }
    .comment-section {
        margin-top: 40px;
    }
    .comment-form {
        display: flex;
        flex-direction: column;
        gap: 10px;
    }
    .comment-form input, .comment-form textarea {
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-size: 16px;
    }
    .comment-form textarea {
        resize: vertical;
    }
    .comment {
        border: 1px solid #ccc;
        padding: 10px;
        border-radius: 5px;
        background-color: #fff;
        box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
        margin-bottom: 10px;
    }
    .comment .comment-header {
        font-weight: bold;
        margin-bottom: 5px;
        display: flex;
        justify-content: space-between;
    }
    .comment .comment-content {
        white-space: pre-wrap;
    }
</style>
<script>
    function deleteBoard(boardno) {
        if (confirm("정말 삭제하시겠습니까?")) {
            location.href = '/board/delete?boardno=' + boardno;
        }
    }
    
    document.addEventListener('DOMContentLoaded', (event) => {
        const contentElement = document.getElementById('board-content');
        const content = contentElement.innerText;

        if (content.length > 1000) {
            contentElement.innerText = content.substring(0, 1000) + '...';
            alert('내용은 1000자 제한입니다');
        }
    });

</script>
</head>
<body>
<div class="container">

    <h2>게시글 상세</h2>
    <table>
        <tr>
            <th>제목</th>
            <td>${board.title}</td>
        </tr>
        <tr>
            <th>작성자</th>
            <td>${board.writer}</td>
        </tr>
        <tr>
            <th>내용</th>
            <td><pre id="board-content" style="font-family: inherit;">${board.content}</pre></td>
        </tr>
        <tr>
            <th>작성일</th>
            <td>${board.createdate}</td>
        </tr>
        <tr>
            <th>조회수</th>
            <td>${board.hit}</td>
        </tr>
    </table>
    
    <!-- 뒤로가기, 수정버튼, 삭제버튼  -->
    <div class="form-group">
        <input type="button" value="목록으로" class="btn-blue" onclick="location.href='/board/'">
        <input type="button" value="수정하기" class="btn-green" onclick="location.href='/board/update?boardno=${board.boardno}'">
        <input type="button" value="삭제하기" class="btn-red" onclick="deleteBoard(${board.boardno})">
    </div>
    
    <!-- 댓글 작성 -->
    <div class="comment-section">
    
        <h3>댓글</h3>
        
        <form class="comment-form" action="/comment/addComment" method="post">
            <input type="hidden" name="boardno" value="${board.boardno}">
            <input type="hidden" name="writer" value="${sessionScope.username}">
            <label for="comment-writer">작성자:</label>
            <input type="text" id="comment-writer" name="writer" value="${sessionScope.username}" readonly>
            <label for="comment-content">내용:</label>
            <textarea id="comment-content" name="content" rows="4" required></textarea>
            <input type="submit" value="댓글 작성" class="btn-green">
        </form>
        
        <!-- 댓글 리스트 -->
        <div class="comment-list">
            <c:forEach var="comment" items="${comments}">
                <div class="comment">
                <div class="comment-header">
	                <span>${comment.writer}</span>
	                <span><fmt:formatDate value="${comment.createdate}" pattern="yy-MM-dd HH:mm:ss"/></span>
                </div>
                <div class="comment-content">${comment.content}</div>
                </div>
            </c:forEach>
        </div>
        
    </div>
    
</div>
</body>
</html>
