<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>채팅방 목록</title>
</head>
<body>
    <table border="1">
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
