package com.securityloginadv.demo.security;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.securityloginadv.demo.constant.ResponseCode;
import com.securityloginadv.demo.result.RestResult;
import com.securityloginadv.demo.util.ServletUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * Kaptcha filter
 *
 * @author liuhongdi
 *
 */
public class KaptchaFilter extends AbstractAuthenticationProcessingFilter {

    // parameter name
    private static final String VRIFYCODE ="vrifyCode";

    // 拦截请求地址
    private String servletPath;

    public KaptchaFilter(String servletPath, String failureUrl) {
        super(servletPath);
        this.servletPath = servletPath;
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(failureUrl));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;


        System.out.println("kaptchafilter:servletPath:"+servletPath);
        System.out.println("kaptchafilter:currentPath:"+req.getServletPath());
        System.out.println("kaptchafilter:current method:"+req.getMethod());
        System.out.println("kaptchafilter:current VRIFYCODE:"+req.getParameter(VRIFYCODE));

        if ("POST".equalsIgnoreCase(req.getMethod()) && servletPath.equals(req.getServletPath())) {
            String expect = (String) req.getSession().getAttribute(VRIFYCODE);

            if (expect != null && !expect.equalsIgnoreCase(req.getParameter(VRIFYCODE))) {
                System.out.println("kaptchafilter: vrifycode is not right");
                //unsuccessfulAuthentication(req, res, new InsufficientAuthenticationException("输入的验证码不正确"));


                /*
                Map<String, String> map2 = new HashMap<String, String>();

                map2.put("status", "1");
                map2.put("msg", "图形验证码未输入或输入错误");
                map2.put("data", "");


                JSONObject jsonObj = new JSONObject(map2);//转化为json格式

                //String json = new Gson().toJson(jsonData);//包装成Json 发送的前台
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();

                out.write(jsonObj.toString());
                out.flush();
                out.close();
                */
                ;
                ServletUtil.printRestResult(RestResult.error(ResponseCode.AUTHCODE_INVALID));
                return;
            } else {
                System.out.println("kaptchafilter: vrifycode is right");
            }
        } else {
            System.out.println("kaptchafilter:not post");
        }
        chain.doFilter(req, res);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        // TODO Auto-generated method stub
        return null;
    }

}
