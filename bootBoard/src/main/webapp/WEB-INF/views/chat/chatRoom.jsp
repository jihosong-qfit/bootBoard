<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>채팅방: ${chatRoom.name}</title>
    <style>
        #chatContent {
            border: 1px solid #ddd;
            height: 300px;
            overflow-y: scroll;
            margin-bottom: 10px;
        }
        #userList {
            list-style-type: none;
            padding: 0;
        }
        #userList li {
            margin: 5px 0;
        }
        .whisper {
            color: grey;
        }
        .admin {
            color: red;
        }
    </style>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <div>
        <h2>채팅방: ${chatRoom.name}</h2>
        <div id="chatContent">
            <!-- 채팅 내용 출력 -->
        </div>
        <form id="chatForm">
            <input type="text" id="message" name="message">
            <input type="hidden" id="sender" name="sender" value="${sessionScope.memberId}">
            <button type="submit">전송</button>
        </form>
        <button id="exitButton">나가기</button>
        <h3>접속자 목록</h3>
        <ul id="userList">
            <c:forEach var="user" items="${chatRoom.members}">
                <li data-username="${user.name}" data-memberid="${user.memberId}">
                    ${user.name}
                    <c:if test="${chatRoom.host eq user.memberId}">(방장)</c:if>
                    
                    <button class="whisperButton">귓속말</button>
                    
                    <c:if test="${chatRoom.host eq user.memberId}">
                        <c:if test="${not chatRoom.host eq user.memberId}">
                            <button class="kickButton">강퇴</button>
                        </c:if>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
    </div>
    <script>
    	const sender = $('#sender').val();
    	const chatRoomHost = '${chatRoom.host}';
    	let isWhisperMode = false; //귓속말모드|일반모드
    	let whisperRecipient = null;
    	
    	 document.getElementById('exitButton').addEventListener('click', function() {
             if (confirm('채팅방이 종료됩니다. 나가시겠습니까?')) {
                 $.ajax({
                     url: '${pageContext.request.contextPath}/chat/removeUser',
                     type: 'POST',
                     data: JSON.stringify({
                         chatRoomId: ${chatRoom.id},
                         memberId: sender
                     }),
                     contentType: 'application/json',
                     success: function() {
                         window.location.href = '${pageContext.request.contextPath}/chat/chatList';
                     }
                 });
             }
         });

    	 window.addEventListener('beforeunload', function(event) {
             const payload = JSON.stringify({
                 chatRoomId: ${chatRoom.id},
                 memberId: sender
             });
             navigator.sendBeacon('${pageContext.request.contextPath}/chat/removeUser', payload);
         });
    	 
        <!-- 귓속말 -->
        $(document).on('click', '.whisperButton', function() {
            const username = $(this).parent().data('username');
            whisperRecipient = username;
            isWhisperMode = true;
            alert(username + "님에게 귓속말 모드가 활성화되었습니다. 이제 메시지를 입력하세요.");
        });
        
        <!-- 채팅메시지 전송 -->
        document.getElementById('chatForm').addEventListener('submit', function(event) {
            event.preventDefault();
            const message = document.getElementById('message').value;
            const chatRoomId = ${chatRoom.id};
            console.log("채팅방 ID: " + chatRoomId);  // 콘솔에서 채팅방 ID를 확인
            console.log("송신자: " + sender);  // 콘솔에서 송신자 확인
            console.log("isWhisperMode: "+isWhisperMode);
            console.log("whisperRecipient: "+whisperRecipient);

            if (isWhisperMode && whisperRecipient) {
                $.ajax({
                    url: '${pageContext.request.contextPath}/chat/sendWhisper',
                    type: 'POST',
                    data: JSON.stringify({
                        recipient: whisperRecipient,
                        message: message,
                        chatRoomId: chatRoomId,
                        sender: sender
                    }),
                    contentType: 'application/json',
                    success: function(response) {
                        const chatContent = document.getElementById('chatContent');
                        console.log(response);
                        console.log(response.whisper);
                        if (response.whisper) {
                            chatContent.innerHTML += '<p class="whisper">' + '[귓] ' + response.sender + " -> " + response.recipient + ': ' + response.message + '</p>';
                            console.log("귓전송")
                        }
                        document.getElementById('message').value = '';
                        isWhisperMode = false; // 귓속말 모드 해제
                        whisperRecipient = null; // 귓속말 대상자 초기화
                    },
                    error: function(xhr, status, error) {
                        console.error('Error:', error);
                        alert('귓속말 전송에 실패했습니다.');
                        isWhisperMode = false; // 귓속말 모드 해제
                        whisperRecipient = null; // 귓속말 대상자 초기화
                    }
                });
            } else {
                $.ajax({
                    url: '${pageContext.request.contextPath}/chat/sendMessage',
                    type: 'POST',
                    data: JSON.stringify({
                        message: message,
                        chatRoomId: chatRoomId,
                        sender: sender
                    }),
                    contentType: 'application/json',
                    success: function(response) {
                        const chatContent = document.getElementById('chatContent');
                        chatContent.innerHTML += '<p>' + response.sender + ':::::: ' + response.message + '</p>';
                        console.log("일반전송")
                        document.getElementById('message').value = '';
                    },
                    
                });
            }
        });

       /*  $(document).on('click', '.whisperButton', function() {
            const username = $(this).parent().data('username');
            const memberId = $(this).parent().data('memberid');
            console.log(username);
            console.log(memberId);
            const message = prompt('귓속말 메시지를 입력하세요:');
            if (message) {
                const recipient = username;
                $.ajax({
                    url: '${pageContext.request.contextPath}/chat/sendWhisper',
                    type: 'POST',
                    data: JSON.stringify({
                        recipient: recipient,
                        message: message,
                        chatRoomId: ${chatRoom.id},
                        sender: sender
                    }),
                    contentType: 'application/json',
                    success: function(response) {
                        const chatContent = document.getElementById('chatContent');
                        if (response.recipient === sender || response.sender === sender) {
                            chatContent.innerHTML += '<p class="whisper">' + '[귓] ' + response.sender + " -> " + response.recipient + ': ' + response.message + '</p>';
                        }
                        document.getElementById('message').value = '';
                    }
                });
            }
        }); */
        
        <!-- 강퇴 -->
        document.querySelectorAll('.kickButton').forEach(button => {
            button.addEventListener('click', function() {
                const username = this.parentElement.getAttribute('data-username');
                // AJAX를 사용하여 유저 강퇴
                $.ajax({
                    url: '${pageContext.request.contextPath}/chat/kickUser',
                    type: 'POST',
                    data: {
                        username: name,
                        chatRoomId: '${chatRoom.id}'
                    },
                    success: function(response) {
                        alert(response);
                        location.reload();
                    }
                });
            });
        });

        <!-- 실시간 메시지 갱신 -->
        function getMessages() {
            $.ajax({
                url: '${pageContext.request.contextPath}/chat/getMessages',
                type: 'GET',
                data: {
                    chatRoomId: '${chatRoom.id}'
                },
                success: function(response) {
                    const chatContent = document.getElementById('chatContent');
                    chatContent.innerHTML = ''; // 기존 메시지를 지우지 않음
                    response.forEach(function(message) {
                        if (message.whisper) {
                            chatContent.innerHTML += '<p class="whisper">' + '[귓] ' + message.sender + " -> " + message.recipient + ': ' + message.message + '</p>';
                            console.log("귓갱신")
                        } else {
                            chatContent.innerHTML += '<p>' + message.sender + ': ' + message.message + '</p>';
                            console.log("일반갱신")
                        }
                    });
                }
            });
        }

        <!-- 입장 시 접속자 목록 업데이트 -->
        function getUsers() {
            $.ajax({
                url: '${pageContext.request.contextPath}/chat/getUsersInRoom',
                type: 'GET',
                data: { chatRoomId: ${chatRoom.id} },
                success: function(users) {
                    const userList = document.getElementById('userList');
                    userList.innerHTML = '';
                    users.forEach(function(user) {
                    	const isHost = user.memberId === chatRoomHost;
                        const isCurrentUserHost = sender === chatRoomHost;
                        userList.innerHTML += '<li data-username="' + user.name + '">' + user.name +
                                              (isHost ? ' (방장)' : '') +
                                              '<button class="whisperButton">귓속말</button>' +
                                              (isCurrentUserHost && !isHost ? '<button class="kickButton">강퇴</button>' : '') +
                                              '</li>';
                    });
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.log('Error: ' + textStatus);
                    console.log('Error Thrown: ' + errorThrown);
                }
            });
        }

        setInterval(getMessages, 1000);
        setInterval(getUsers, 1000);
        

        $(document).on('click', '.kickButton', function() {
            const username = $(this).parent().data('username');
            $.ajax({
                url: '${pageContext.request.contextPath}/chat/kickUser',
                type: 'POST',
                data: JSON.stringify({
                    username: username,
                    chatRoomId: ${chatRoom.id}
                }),
                contentType: 'application/json',
                success: function(response) {
                    alert(response);
                    location.reload();
                }
            });
        });
    </script>
</body>
</html>
