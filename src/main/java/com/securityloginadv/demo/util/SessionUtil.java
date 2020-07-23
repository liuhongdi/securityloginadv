package com.securityloginadv.demo.util;

import com.securityloginadv.demo.security.SecUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

public class SessionUtil {

    //得到security所保存的用户
    public static SecUser getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                //System.out.println("-----------------------------type userDetails");
                SecUser currentuser = (SecUser) principal;
                return currentuser;
            }

            return null;
        }
        return null;
    }

    //设置session
    public static void setsession(String key,Object value) {
        currentSession().setAttribute(key,value);
    }

    //得到指定的session值
    public static Object getsession(String key) {
        return currentSession().getAttribute(key);
    }
    
    //删除所有session
    public static void deleteSession() {
        currentSession().invalidate();
    }

    //得到所有session
    public static HttpSession currentSession() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
    }
}
