package com.securityloginadv.demo.security;

//import org.json.JSONObject;
import com.securityloginadv.demo.result.RestResult;
import com.securityloginadv.demo.util.ServletUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liuhongdi on 2019/12/12.
 *
 * 用户登出处理类
 */
@Component("UserLogoutSuccessHandler")
public class UserLogoutSuccessHandler implements LogoutSuccessHandler{
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //System.out.println("===================================logout success!");
        httpServletRequest.getSession().invalidate();
        ServletUtil.printRestResult(RestResult.success(0,"退出成功"));
    }
}
