<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 페이지</title>
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
        width: 300px;
        padding: 20px;
        border: 1px solid #ccc;
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
    .form-group input[type="text"], .form-group input[type="password"] {
        width: 100%;
        padding: 10px;
        font-size: 16px;
        border: 1px solid #ccc;
        border-radius: 5px;
        box-sizing: border-box;
    }
    .form-group input[type="submit"] {
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
    .link {
        text-align: right;
        margin-top: 20px;
    }
    .link a {
        text-decoration: none;
        color: #007bff;
    }
    .link a:hover {
        text-decoration: underline;
    }
    .error {
        color: red;
        margin-bottom: 15px;
        text-align: center;
    }
</style>
</head>
<body>
<div class="container">
    <form action="/member/login" method="post">
        <div class="form-group">
            <label for="memberId">아이디:</label>
            <input type="text" id="memberId" name="memberId">
        </div>
        <div class="form-group">
            <label for="password">비밀번호:</label>
            <input type="password" id="password" name="password">
        </div>
        <div class="form-group">
            <input type="submit" value="로그인">
        </div>
        <div class="link">
        	<a href="/member/join">회원가입</a>
    	</div>
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
    </form>
</div>
</body>
</html>
