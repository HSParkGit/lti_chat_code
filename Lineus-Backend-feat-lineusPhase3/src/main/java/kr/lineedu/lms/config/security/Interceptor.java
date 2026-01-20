package kr.lineedu.lms.config.security;

//import java.io.PrintWriter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import com.lineedu.portal.domain.account.permission.repository.RoleMenu;
//import com.lineedu.portal.domain.account.permission.repository.RoleMenuRepository;
//import com.lineedu.portal.domain.account.user.repository.kr.lineedu.cha.domain.user.UserRepository;
//import com.lineedu.portal.domain.common.define.WorkflowState;
//import com.lineedu.portal.utils.SessionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Interceptor {
	// springboot에서 사용하는 Interceptor로 재구성

}

// 샘플코드
//public class Interceptor extends HandlerInterceptorAdapter {

//Log log = LogFactory.getLog(this.getClass());

/*    private final kr.lineedu.cha.domain.user.UserRepository userRepository;
    private final RoleMenuRepository roleMenuRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = (String) request.getRequestURI();
        String menuId = (String) request.getParameter("menuId");
        String subMenuId = (String) request.getParameter("subMenuId");
        logger.info("[nterceptor preHandle] uri = " + uri);
        logger.info("[nterceptor preHandle] menuId =  " + menuId);
        logger.info("[nterceptor preHandle] subMenuId = " + subMenuId);

        logger.info("[nterceptor preHandle] System.getProperty spring.profiles.active = " + System.getProperty("spring.profiles.active"));
        // local에서 개발할때 세션이 자꾸 끊어지면 개발이 힘들어서 개발의 용이성을 위해
        // 다른 ROLE를 테스트할려면 여기를 주석으로 막아야 함.
        if (System.getProperty("spring.profiles.active").equals("local")) {
            // 임시 로그인 처리(개발을 용이하게 하기 위해). 실서버 적용에는 주석처리가 되어야 함.
            new SessionManager(request).setLoginInfo(userRepository.findByWorkflowStateAndLoginIdAndPassword(
                    WorkflowState.active,
                    "superadmin",
                    "7ee80f7d7ac4404a7b1fe29eae202e15278c5edb"));
            // 로그인 처리
        }

        if (!SessionManager.isLoin(request)) {
            log.info("Interceptor preHandle isLogin false");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.println("<script>window.location.href = '" + request.getContextPath() + "/login.do';</script>");
            return false;
        } else {
            log.info("Interceptor preHandle isLogin true");
            log.info("Interceptor preHandle 1111111111111111111 === " + uri + "=" + uri.indexOf("/main.do"));
            log.info("Interceptor preHandle 1111111111111111111 === " + uri + "=" + uri.indexOf("/api/"));
            if (uri.indexOf("/main.do") <= -1 && uri.indexOf("/api/") <= -1) {
                log.info("Interceptor preHandle 1111111111111111111");
                if (menuId != null) {
                    log.info("Interceptor preHandle menuId");
                    RoleMenu roleMenu = roleMenuRepository.findByWorkflowStateAndRoleIdAndMenuId(
                            WorkflowState.active,
                            SessionManager.getLoginInfo(request).getRole().getId(),
                            Long.parseLong(menuId));
                    //log.info("Interceptor preHandle menuId roleMenu = " + roleMenu.toString());
                    if (roleMenu == null) {
                        log.info("Interceptor preHandle menuId roleMenu is null");
                        response.setCharacterEncoding("utf-8");
                        response.setContentType("text/html;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        out.println("<script>window.location.href = '/" + request.getContextPath() + "/error/notAccess.do';</script>");
                        return false;
                    }
                }
                if (subMenuId != null) {
                    log.info("Interceptor preHandle subMenuId");
                    RoleMenu roleMenu = roleMenuRepository.findByWorkflowStateAndRoleIdAndMenuId(
                            WorkflowState.active,
                            SessionManager.getLoginInfo(request).getRole().getId(),
                            Long.parseLong(subMenuId));
                    //log.info("Interceptor preHandle subMenuId roleMenu = " + roleMenu.toString());
                    if (roleMenu == null) {
                        log.info("Interceptor preHandle subMenuId roleMenu is null");
                        response.setCharacterEncoding("utf-8");
                        response.setContentType("text/html;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        out.println("<script>window.location.href = '/" + request.getContextPath() + "/error/notAccess.do';</script>");
                        return false;
                    }
                }
            }
        }

        return super.preHandle(request, response, handler);
        //return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //logger.info("=========================Interceptor postHandle");
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //logger.info("=========================Interceptor afterCompletion");
    }
}*/
