package kr.lineedu.lms.config.web;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.lineedu.lms.utils.CurrentUserUtil;
import kr.lineedu.lms.utils.LmsUserUtil;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final CurrentUserUtil currentUserUtil;
    private final LmsUserUtil lmsUserUtil;


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserUtil);
        resolvers.add(lmsUserUtil);

        //  Setting One Index Parameter of pagination
        /*PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setOneIndexedParameters(true); // this makes it start from 1
        resolvers.add(resolver);*/
    }

   
}
