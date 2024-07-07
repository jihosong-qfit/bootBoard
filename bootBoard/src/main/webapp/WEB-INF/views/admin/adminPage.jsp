<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 페이지</title>
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
</style>
</head>
<body>
<div class="container">
    <h2>관리자 페이지</h2>
    <div class="links">
        <a href="/board/">게시글 목록으로 이동</a>
    </div>
    <div class="search-box">
        <form action="/admin/search" method="get">
            <input type="text" name="name" placeholder="검색할 이름을 입력하세요">
            <input type="submit" value="검색">
        </form>
    </div>
    
    <table>
        <thead>
            <tr>
                <th>아이디</th>
                <th>이름</th>
                <th>권한</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="member" items="${memberList}">
                <tr>
                    <td>${member.memberId}</td>
                    <td>${member.name}</td>
                    <td>${member.role}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty memberList}">
                <tr>
                    <td colspan="3">회원이 없습니다.</td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>
</body>
</html>
