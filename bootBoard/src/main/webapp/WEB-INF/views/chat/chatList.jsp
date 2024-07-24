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
<input type="hidden" id="userRole" value="${sessionScope.role}"/>
<h2>채팅방 목록</h2>
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
                    <%-- <td><button class="enterRoomButton" data-roomid="${room.id}" data-password="${room.password != null ? 'Y' : 'N'}">입장하기</button></td> --%>
                    <td>
                        <c:choose>
                            <c:when test="${room.connectedUsers >= room.limit && sessionScope.role != 'ROLE_ADMIN'}">
                                <button class="enterRoomButton" disabled>입장 불가</button>
                            </c:when>
                            <c:otherwise>
                                <button class="enterRoomButton" data-roomid="${room.id}" data-password="${room.password != null ? 'Y' : 'N'}">입장하기</button>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
             <c:if test="${empty chatRooms}">
                <tr>
                    <td colspan="5">채팅방이 없습니다.</td>
                </tr>
            </c:if>
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
	function updateConnectedUsers(roomId, memberId, userRole) {
	    $.ajax({
	        type: 'GET',
	        url: '${pageContext.request.contextPath}/chat/getConnectedUsers',
	        contentType: 'application/x-www-form-urlencoded',
	        data: { 
	        	roomId: roomId,
	        	memberId: memberId 
	        },
	        success: function(response) {
	        	console.log("Response from server for room " + roomId + ": ", response);
	            const row = $('button[data-roomid="' + roomId + '"]').closest('tr');
	            
	            if (response.connectedUsers === -1) {
	                row.remove(); // 접속자 수가 0인 채팅방은 목록에서 제거
	            } else {
	                row.find('td').eq(1).text(response.connectedUsers + '/' + response.limit);
	                if (response.connectedUsers >= response.limit && !response.alreadyConnected && userRole !== 'ROLE_ADMIN') {
	                    row.find('.enterRoomButton').prop('disabled', true).text('입장 불가');
	                } else {
	                    row.find('.enterRoomButton').prop('disabled', false).text('입장하기');
	                }
	            }
	            
	            // 남은 채팅방이 없는 경우 메시지 추가
	            if ($('#chatRoomsTable tbody tr').length === 0) {
	                $('#chatRoomsTable tbody').append('<tr><td colspan="5">채팅방이 없습니다.</td></tr>');
	            }
	            
	        },
	        error: function(xhr, status, error) {
	            console.error('Error:', error);
	        }
	    });
	}//end func
	
	function updateAllChatRooms(memberId, userRole) {
	    $('.enterRoomButton').each(function() {
	        var roomId = $(this).data('roomid');
	        console.log("log updateAllchatRooms: " + memberId);
	        updateConnectedUsers(roomId, memberId, userRole);
	    });
	}//end func

	$(document).ready(function() {
    	var memberId = $('#memberId').val();
    	var userRole = $('#userRole').val();
        setInterval(function() {
        	updateAllChatRooms(memberId, userRole);
        }, 1000);
    
	// 입장하기 버튼 클릭 이벤트 처리
    $(document).on('click', '.enterRoomButton', function() {
        var roomId = $(this).data('roomid');
        var hasPassword = $(this).data('password') === 'Y';
        var memberId = $('#memberId').val();
        var userRole = $('#userRole').val();        
        
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
                                enterChatRoom(roomId, memberId);
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
        	enterChatRoom(roomId, memberId, userRole);
        }
    });
	
    function enterChatRoom(roomId, memberId, userRole) {
	    $.ajax({
	        url: '${pageContext.request.contextPath}/chat/enterRoom',
	        type: 'POST',
	        contentType: 'application/json',
	        data: JSON.stringify({
	            roomId: roomId,
	            memberId: memberId,
	            userRole: userRole
	        }),
	        success: function() {
	            window.location.href = '${pageContext.request.contextPath}/chat/chatRoom/' + roomId;
	        },
	        error: function(xhr) {
	            if (xhr.responseText) {
	                alert(xhr.responseText);
	            } else {
	                alert('채팅방 입장에 실패했습니다.');
	            }
	        }
	    });
	}
});
</script>
</html>
