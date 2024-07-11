<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성 페이지</title>
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
    .form-group {
        margin-bottom: 15px;
    }
    .form-group label {
        display: block;
        font-weight: bold;
        margin-bottom: 5px;
    }
    .form-group input[type="text"], 
    .form-group textarea {
        width: 100%;
        padding: 10px;
        font-size: 16px;
        border: 1px solid #ccc;
        border-radius: 5px;
        box-sizing: border-box;
    }
    .form-group textarea {
        height: 200px;
    }
    .form-group input[type="submit"] {
        display: block;
        width: 100%;
        padding: 10px;
        font-size: 18px;
        color: #fff;
        background-color: #007bff;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }
    .form-group input[type="submit"]:hover {
        background-color: #0056b3;
    }
    .h1 {
    	text-align: center;
    	justify-content: center;
    }
</style>
</head>
<body>
<div class="container">
    <form action="/board/save" method="post">
     <input type="hidden" name="memberId" value="${sessionScope.member_id}">
        <div class="form-group">
            <label for="title">제목:</label>
            <input type="text" id="title" name="title" required="required">
        </div>
        <div class="form-group">
            <label for="writer">작성자:</label>
            <input type="text" id="writer" name="writer" required="required">
        </div>
        <div class="form-group">
            <label for="content">내용:</label>
            <textarea id="content" name="content" placeholder="내용을 입력하세요" required="required"></textarea>
        </div>
        <div class="form-group">
            <input type="submit" value="작성">
        </div>
    </form>
</div>
</body>
</html>
