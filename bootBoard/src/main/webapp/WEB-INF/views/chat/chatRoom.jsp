<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Chat Room</title>
</head>
<body>
    <div>
        <h2>Chat Room: ${chatRoom.name}</h2>
        <div id="chatContent">
            <!-- 채팅 내용 출력 -->
        </div>
        <form id="chatForm">
            <input type="text" id="message" name="message">
            <button type="submit">Send</button>
        </form>
        <button id="exitButton">Exit</button>
    </div>
    <script>
        // 채팅 기능 구현
        document.getElementById('exitButton').addEventListener('click', function() {
            if (confirm('채팅방이 종료됩니다. 나가시겠습니까?')) {
                window.location.href = '${pageContext.request.contextPath}/chat/chatList';
            }
        });
    </script>
</body>
</html>
