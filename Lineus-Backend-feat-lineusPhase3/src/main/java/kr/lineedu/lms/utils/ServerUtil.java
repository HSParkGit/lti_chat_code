package kr.lineedu.lms.utils;

import jakarta.servlet.http.HttpServletRequest;

public class ServerUtil {

    public static String getServerRoot(HttpServletRequest request) {

        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String forwardedHost = request.getHeader("X-Forwarded-Host");
        String forwardedPort = request.getHeader("X-Forwarded-Port");
        if (forwardedPort == null || forwardedPort == "") {
            return scheme + "://" + serverName + ":" + serverPort + "/";
        }
        return scheme + "://" + forwardedHost + ":" + forwardedPort + "/";

    }
}
