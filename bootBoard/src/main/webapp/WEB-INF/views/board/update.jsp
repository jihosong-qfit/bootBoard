<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정 페이지</title>
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
    .form-group input[type="submit"],
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
</style>
</head>
<body>
<div class="container">
    <h2>게시글 수정</h2>
    <form action="/board/update" method="post" name="updateForm">
        <table>
            <tr>
                <th>제목</th>
                <td><input type="text" name="title" value="${board.title}" required></td>
            </tr>
            <tr>
                <th>작성자</th>
                <td><input type="text" name="writer" value="${board.writer}" readonly="readonly"></td>
            </tr>
            <tr>
                <th>내용</th>
                <td>
                	<textarea name="content" rows="10" required>${board.content}</textarea>
                </td>
            </tr>
            <input type="hidden" name="boardno" value="${board.boardno}">
        </table>
        <div class="form-group">
            <input type="submit" value="수정완료" class="btn-green">
            <input type="button" value="취소" class="btn-blue" onclick="location.href='/board/update?boardno=${board.boardno}'">
        </div>
    </form>
</div>
</body>
</html>
