package com.jonghak.springbootweb.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

public class VisitTimeInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute("visitTime") == null) {
            httpSession.setAttribute("visitTime", LocalDateTime.now());
        }
        return true;
    }
}
