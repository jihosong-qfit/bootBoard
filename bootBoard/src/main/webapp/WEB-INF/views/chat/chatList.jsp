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
                <th>입장</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="room" items="${chatRooms}">
                <tr>
                    <td>${room.name}</td>
                    <td>${room.connectedUsers}/${room.limit}</td>
                    <td>${room.host}</td>
                    <td>${room.password != null ? 'Y' : 'N'}</td>
                    <td><button class="enterRoomButton" data-roomid="${room.id}" data-password="${room.password != null ? 'Y' : 'N'}">입장하기</button></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    <!-- 비밀번호 입력 모달 -->
    <div id="passwordModal" style="display:none;">
        <div>
            <h2>* 채팅방 비밀번호를 입력해주세요!</h2>
            <input type="password" id="roomPassword" placeholder="비밀번호 입력"/>
            <button id="submitPassword">입장하기</button>
            <button id="cancelPassword">취소</button>
        </div>
    </div>
    
</body>
<script>
//특정 채팅방 접속자 수만 업데이트
	function updateConnectedUsers(roomId) {
	    $.ajax({
	        url: '${pageContext.request.contextPath}/chat/getConnectedUsers',
	        type: 'GET',
	        data: { roomId: roomId },
	        success: function(response) {
	            const row = $('button[data-roomid="' + roomId + '"]').closest('tr');
	            row.find('td').eq(1).text(response.connectedUsers + '/' + response.limit);
	        },
	        error: function(xhr, status, error) {
	            console.error('Error:', error);
	        }
	    });
	}
	
	function updateAllChatRooms() {
	    $('.enterRoomButton').each(function() {
	        var roomId = $(this).data('roomid');
	        updateConnectedUsers(roomId);
	    });
	}

	// 입장하기 버튼 클릭 이벤트 처리
    $(document).on('click', '.enterRoomButton', function() {
        var roomId = $(this).data('roomid');
        var hasPassword = $(this).data('password') === 'Y';
        
        if (hasPassword) {
            $('#passwordModal').show();
            $('#submitPassword').off('click').on('click', function() {
                var password = $('#roomPassword').val();
                if (password) {
                    // AJAX 요청으로 비밀번호 검증
                    $.ajax({
                        url: '${pageContext.request.contextPath}/chat/validatePassword',
                        type: 'POST',
                      	contentType: 'application/json',
                        data: JSON.stringify({
                   			 roomId: roomId,
                   			 password: password
                		}),
                        success: function(response) {
                            if (response.valid) {
                                window.location.href = '${pageContext.request.contextPath}/chat/chatRoom/' + roomId;
                            } else {
                                alert('비밀번호가 틀렸습니다. 비밀번호를 다시 입력해주세요!');
                            }
                        }
                    });
                } else {
                    alert('비밀번호 입력창에 비밀번호를 입력하세요!');
                }
            });
            $('#cancelPassword').off('click').on('click', function() {
                $('#passwordModal').hide();
            });
        } else {
        		window.location.href = '${pageContext.request.contextPath}/chat/chatRoom/' + roomId;	
        }
    });
	
    $(document).ready(function() {
        setInterval(updateAllChatRooms, 5000);
        
    });
    </script>
</html>
