package com.securityloginadv.demo.interceptor;

import com.securityloginadv.demo.security.SecUser;
import com.securityloginadv.demo.util.SessionUtil;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class WebInterceptor extends HandlerInterceptorAdapter {

    //如果view不为空，把登录信息传递给模板
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null) {
            ModelMap modelMap = modelAndView.getModelMap();
            SecUser currentUser = SessionUtil.getCurrentUser();
            if (currentUser != null) {
                modelMap.addAttribute("is_login","1");
                modelMap.addAttribute("login_username",currentUser.getNickname());
            } else {
                modelMap.addAttribute("is_login","0");
                modelMap.addAttribute("login_username","");
            }
        }
    }

}
