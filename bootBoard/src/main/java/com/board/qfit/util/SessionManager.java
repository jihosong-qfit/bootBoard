package com.board.qfit.util;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpSession;

@Component
//강제퇴장을 할 유저 세션에 저장
public class SessionManager {

    private final ConcurrentHashMap<String, HttpSession> sessions = new ConcurrentHashMap<>();

    //세션 등록
    public void registerSession(String userId, HttpSession session) {
        sessions.put(userId, session);
    }

    //등록된 세션에서 값 가져오기
    public HttpSession getSession(String userId) {
        return sessions.get(userId);
    }

    //세션 삭제
    public void removeSession(String userId) {
        sessions.remove(userId);
    }
}
