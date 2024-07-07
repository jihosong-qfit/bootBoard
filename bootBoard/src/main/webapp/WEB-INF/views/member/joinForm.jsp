<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입 페이지</title>
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
    tr:nth-child(even) {
        background-color: #f2f2f2;
    }
    .form-group {
        margin-top: 20px;
        text-align: right;
    }
    .form-group input[type="button"], .form-group input[type="submit"] {
        padding: 10px 20px;
        font-size: 16px;
        color: #fff;
        background-color: #007bff;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }
    .form-group input[type="button"]:hover, .form-group input[type="submit"]:hover {
        background-color: #0056b3;
    }
    .link {
        text-align: center;
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
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
$(document).ready(function() {
    $("#memberId").on("change", function() {
        var memberId = $(this).val();
        $.ajax({
            url: "/member/checkId",
            type: "GET",
            data: { memberId: memberId },
            success: function(response) {
                if (response) {
                    $("#idError").text("중복된 아이디입니다. 다른 아이디로 입력해주세요!");
                } else {
                    $("#idError").text("");
                }
            }
        });
    });
});
</script>
</head>
<body>
<div class="container">
    <h2>회원가입</h2>
    <form action="/member/join" method="post">
        <table>
            <tr>
                <th>아이디(필수)</th>
                <td>
                	<input type="text" name="memberId" required>
                	<div id="idError" class="error"></div>
                </td>
            </tr>
            <tr>
                <th>이름(필수)</th>
                <td><input type="text" name="name" required></td>
            </tr>
            <tr>
                <th>닉네임(선택)</th>
                <td><input type="text" name="nickname"></td>
            </tr>
            <tr>
                <th>비밀번호(필수)</th>
                <td><input type="password" name="password" required></td>
            </tr>
            <tr>
                <th>비밀번호 확인(필수)</th>
                <td><input type="password" name="confirmPassword" required></td>
            </tr>
        </table>
        <div class="form-group">
            <input type="submit" value="가입">
        </div>
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
    </form>
    <div class="link">
        <a href="/member/login">로그인창으로</a>
    </div>
</div>
</body>
</html>
