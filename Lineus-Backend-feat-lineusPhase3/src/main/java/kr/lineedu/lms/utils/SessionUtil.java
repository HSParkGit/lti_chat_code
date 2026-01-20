package kr.lineedu.lms.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.lineedu.lms.config.jwt.JwtTokenProvider;
import kr.lineedu.lms.global.error.dto.ErrorCode;
import kr.lineedu.lms.global.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionUtil {

    public static final int MAX_SESSION_TIME = 60 * 60 * 2; // 2시간
    public HttpServletRequest request;
    public HttpSession session;

    public SessionUtil(HttpServletRequest request) {
        HttpSession currentSession = request.getSession();
        currentSession.setMaxInactiveInterval(MAX_SESSION_TIME);
        setSession(currentSession);
        setRequest(request);
    }

    public void setLoginInfo(String loginId, String path) {
        try {
            setSession("loginId", loginId);
            setSession("path", path);
        } catch (Exception e) {
            log.error("Session Error : {}", e.getMessage());
        }
    }

    public static String getLoginId(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (String) session.getAttribute("loginId");
    }
    public static void deniedIfStudent() {
        if(JwtTokenProvider.isStudentRole()){
            throw new BusinessException(ErrorCode.NOT_ALLOWED_USER_EXCEPTION);
        }
    }

    public static String getPath(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (String) session.getAttribute("path");
    }

    private void setSession(HttpSession session) {
        this.session = session;
    }

    private void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    private void setSession(String key, Object value) {
        this.session.setAttribute(key, value);
    }
}
