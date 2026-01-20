/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.servlet.http.HttpServletRequest
 *  net.lomtech.helloworld.lti.util.UrlUtil
 */
package net.lomtech.lti.util;

import jakarta.servlet.http.HttpServletRequest;

public class UrlUtil {
    public static String getBaseUrlFromRequest(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String baseUrl = scheme + "://" + serverName + (String)(serverPort == 80 || serverPort == 443 ? "" : ":" + serverPort);
        return baseUrl;
    }
}

