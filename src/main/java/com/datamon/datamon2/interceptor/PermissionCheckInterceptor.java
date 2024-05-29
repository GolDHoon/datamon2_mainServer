package com.datamon.datamon2.interceptor;

import com.datamon.datamon2.controller.UserSignController;
import com.datamon.datamon2.servcie.logic.UserSignService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class PermissionCheckInterceptor implements HandlerInterceptor {
    private static final Logger logger = LogManager.getLogger(UserSignController.class);
    @Autowired
    private UserSignService userSignService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String result;

        String url = request.getHeader("url");
        String domain = request.getHeader("domain");
        String path = request.getHeader("Path");

        try {
            result = userSignService.sessionCheck(request);
        } catch (Exception e) {
            logger.error(e);
            result = "error";
        }

        if(!"success".equals(result)) {
            response.getWriter().write(result);
            return false; // Returning false so that request does not further proceed
        }

        return true; // if 'success', return true to proceed the request
    }
}
