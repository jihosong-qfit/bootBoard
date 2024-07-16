<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 목록 페이지</title>
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
    .links {
        text-align: center;
        margin-bottom: 20px;
    }
    .links a {
        margin: 0 15px;
        text-decoration: none;
        color: #007bff;
    }
    .links a:hover {
        text-decoration: underline;
    }
    .search-box {
        text-align: center;
        margin-bottom: 20px;
    }
    .search-box input[type="text"] {
        padding: 10px;
        font-size: 16px;
        width: 80%;
        max-width: 400px;
    }
    .search-box input[type="submit"] {
        padding: 10px 20px;
        font-size: 16px;
        color: #fff;
        background-color: #007bff;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }
    .search-box input[type="submit"]:hover {
        background-color: #0056b3;
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
    tr:nth-child(even) {
        background-color: #f2f2f2;
    }
    .form-group {
        margin-top: 20px;
        text-align: right;
    }
    .form-group input[type="button"] {
        padding: 10px 20px;
        font-size: 16px;
        color: #fff;
        background-color: #007bff;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }
    .form-group input[type="button"]:hover {
        background-color: #0056b3;
    }
    .pagination {
        display: flex;
        justify-content: center;
        list-style-type: none;
        padding: 0;
    }
    .pagination li {
        margin: 0 5px;
    }
    .pagination a {
        color: #007bff;
        text-decoration: none;
        padding: 5px 10px;
        border: 1px solid #007bff;
        border-radius: 5px;
    }
    .pagination a:hover {
        background-color: #007bff;
        color: white;
    }
    .selectBox {
    	text-align: center;
        margin-bottom: 20px;
        padding: 10px;
        font-size: 16px;
    }
</style>
</head>
<body>
<div class="container">
    <h2>게시글 목록</h2><br>
    <div class="links">
	    <a href="/member/logout">로그아웃</a>
	    <c:if test="${sessionScope.role == 'ROLE_ADMIN'}">
            <a href="/admin/adminPage">게시판관리</a>
        </c:if>
        <a href="/chat/chatCreate">채팅방 만들기</a>
        <a href="/chat/chatList">채팅방 목록</a>
    </div>
    <div class="search-box">
    <form action="/board/search" method="get">
        <select name="searchType" class="selectBox">
            <option value="title">제목</option>
            <option value="writer">작성자</option>
            <option value="content">내용</option>
        </select>
        <input type="text" name="keyword" placeholder="검색할 내용을 입력하세요">
        <input type="submit" value="검색">
    </form>
</div>
    <table>
        <thead>
            <tr>
                <th>번호</th>
                <th>제목</th>
                <th>작성자</th>
                <th>작성일</th>
                <th>조회수</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="board" items="${boardList}">
                <tr>
                    <td>${board.boardno}</td>
                    <td>
                     <%-- <c:choose>
                    <c:when test="${sessionScope.memberId == board.memberId}"> --%>
                        <a href="/board?boardno=${board.boardno}">${board.title}</a>
                    <%-- </c:when>
                    <c:otherwise>
                        ${board.title}
                    </c:otherwise>
                	 </c:choose> --%>
                    </td>
                    <td>${board.writer}</td>
                    <td>${board.createdate}</td>
                    <td>${board.hit}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty boardList}">
                <tr>
                    <td colspan="5">게시글이 없습니다.</td>
                </tr>
            </c:if>
        </tbody>
    </table>
    <div class="form-group">
        <input type="button" value="작성하기" onclick="location.href='/board/save'">
    </div>
    <ul class="pagination">
        <c:forEach begin="1" end="${totalPages}" var="i">
            <li>
                <a href="?page=${i}&size=${size}">${i}</a>
            </li>
        </c:forEach>
    </ul>
</div>
</body>
</html>
