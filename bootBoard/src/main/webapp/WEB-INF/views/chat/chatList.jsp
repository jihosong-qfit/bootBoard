<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>채팅방 목록</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
    $(document).ready(function() {
    	if (window.location.pathname === '/chat/chatList') {
        function updateChatList() {
            $.ajax({
                url: "${pageContext.request.contextPath}/chat/updateChatList",
                type: "GET",
                dataType: "json",
                cache: false,
                success: function(data) {
                    console.log("AJAX 요청 성공");
                    console.log(data); // 응답 데이터를 콘솔에 출력
                    
                    // 목록 테이블 초기화
                    $("#chatRoomsTable tbody").empty();

                    // 받은 JSON 데이터의 각각의 방 정보 처리
                    $.each(data, function(index, room) {
                        var roomId = room.id;
                        var roomName = room.name;
                        var connectedUsers = room.connectedUsers;
                        var limit = room.limit;
                        var host = room.host;
                        var password = room.password !== '' ? 'Y' : 'N';

                        // 새로운 값들을 동적으로 추가
                        var row = "<tr>" +
                            "<td>" + roomName + "</td>" +
                            "<td>" + connectedUsers + "/" + limit + "</td>" +
                            "<td>" + host + "</td>" +
                            "<td>" + password + "</td>" +
                            "<td><a href='${pageContext.request.contextPath}/chat/chatRoom/" + roomId + "'>입장하기</a></td>" +
                            "</tr>";
                        $("#chatRoomsTable tbody").append(row);
                    });
                },
                error: function(xhr, status, error) {
                    console.error("AJAX 요청 실패");
                    console.error(status + ": " + error);
                }
            });
        }

        // 10초마다 갱신
        setInterval(updateChatList, 10000);
        updateChatList(); // 페이지 로드 시 초기 갱신
    }
    });
    </script>
</head>
<body>
    <table id="chatRoomsTable" border="1">
        <thead>
            <tr>
                <th>채팅방명</th>
                <th>접속자수</th>
                <th>방장</th>
                <th>비밀번호</th>
                <th>입장하기</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="room" items="${chatRooms}">
                <tr>
                    <td>${room.name}</td>
                    <td>${room.connectedUsers}/${room.limit}</td>
                    <td>${room.host}</td>
                    <td>${room.password != null ? 'Y' : 'N'}</td>
                    <td><a href="${pageContext.request.contextPath}/chat/chatRoom/${room.id}">입장하기</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
