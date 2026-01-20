/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.servlet.http.HttpServletRequest
 *  jakarta.servlet.http.HttpServletResponse
 *  net.lomtech.helloworld.lti.service.LTIService
 */
package net.lomtech.lti.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface LTIService {
    public void getLoginForm(String var1, HttpServletResponse var2, Boolean var3, String var4) throws IOException;

    public String initialLoginAndGetRedirectURL(Map<String, String> var1, HttpServletResponse var2, String var3, String var4);

    public String ltiLaunchURIBuilder(Map<String, String> var1, String var2);

    public String getAuthorizedRedirectURL(String var1, Map<String, String> var2, String var3, String var4);

    public String getDemoPageHTMLContent(Map<String, String> var1, HttpServletRequest var2);
}

