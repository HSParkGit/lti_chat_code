package kr.lineedu.lms.utils;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;



import kr.lineedu.lms.config.jwt.JwtPrincipal;
import kr.lineedu.lms.global.annotation.LmsUser;

@Component
public class LmsUserUtil implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LmsUser.class);
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtPrincipal principal = (JwtPrincipal)authentication.getPrincipal();

        return principal.getLmsUserId();
    }


    /**
     * To get the current user's ID from the security context.
     * @author Htoo Maung Thait
     * @co-author Github Copilot
     * @since 2025-07-29
     * @lastModified 2025-07-29
     * @return id of the current user, or null if not authenticated.
     *
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof JwtPrincipal principal) {
            return principal.getLmsUserId();
        }
        return null;
    }
}
