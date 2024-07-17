<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>채팅방 목록</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<input type="hidden" id="memberId" value="${sessionScope.memberId}"/>
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
                    <%-- <td><button class="enterRoomButton" data-roomid="${room.id}">입장하기</button></td> --%>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
<script>
    $(document).ready(function() {
        // 채팅방 입장
        $(document).on('click', '.enterRoomButton', function() {
            const chatRoomId = $(this).data('roomid').toString();
            console.log(chatRoomId);
            const memberId = $('#memberId').val(); // sessionScope.memberId를 사용 가능.

            $.ajax({
                url: '${pageContext.request.contextPath}/chat/enterRoom',
                type: 'POST',
                data: JSON.stringify({
                	chatRoomId: chatRoomId,
                	memberId: memberId 
                }),
                contentType: 'application/json',
                success: function() {
                    window.location.href = '${pageContext.request.contextPath}/chat/chatRoom/' + chatRoomId;
                }
            });
        });

        // 주기적으로 채팅방 목록 갱신
        function updateChatRooms() {
            $.ajax({
                url: '${pageContext.request.contextPath}/chat/getChatRooms',
                type: 'GET',
                success: function(chatRooms) {
                    const chatRoomsTable = $('#chatRoomsTable tbody');
                    chatRoomsTable.empty();
                    chatRooms.forEach(function(room) {
                        chatRoomsTable.append('<tr>' +
                            '<td>' + room.name + '</td>' +
                            '<td>' + room.connectedUsers + '/' + room.limit + '</td>' +
                            '<td>' + room.host + '</td>' +
                            '<td>' + (room.password ? 'Y' : 'N') + '</td>' +
                            '<td><button class="enterRoomButton" data-roomid="' + room.id + '">입장하기</button></td>' +
                            '</tr>');
                    });
                }
            });
        }

        setInterval(updateChatRooms, 5000);
    });
    </script>
</html>
