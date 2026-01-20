/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.lomtech.helloworld.lti.config.WebConfig
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
 *  org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 */
package net.lomtech.lti.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig
implements WebMvcConfigurer {
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(new String[]{"/assets/**"}).addResourceLocations(new String[]{"classpath:/assets/"});
    }
}

