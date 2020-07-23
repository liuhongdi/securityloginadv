package com.securityloginadv.demo.controller;

import com.securityloginadv.demo.security.SecUser;
import com.securityloginadv.demo.util.ServletUtil;
import com.securityloginadv.demo.util.SessionUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/*
*@author:liuhongdi
*@date:2020/7/3 下午3:49
*@description:
 * @param null
*@return:
*/
@Controller
@RequestMapping("/home")
public class HomeController {

    //首页
    @RequestMapping("/home")
    public String index(Model model){

        return "home/home";
    }

    //得到一个bcrypt密码
    @RequestMapping("/getpass")
    @ResponseBody
    public String getpass(@RequestParam("password") String password){
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        return encodedPassword;
    }

    //查看登录后的用户信息
    @RequestMapping("/session")
    @ResponseBody
    public Object getsession(){

        SessionUtil.setsession("age","30");
        //打印所有的session
        HttpSession session = SessionUtil.currentSession();
        java.util.Enumeration   e   =   session.getAttributeNames();

        while( e.hasMoreElements())   {
            String sessionName=(String)e.nextElement();
            System.out.println("\nsession item name="+sessionName);
            System.out.println("\nsession item value="+session.getAttribute(sessionName));
        }

        //打印session的默认时间长度,
        //默认时间:1800 seconds
        System.out.println("这个session的时间长度:"+session.getMaxInactiveInterval());

        long timeNum = session.getLastAccessedTime();
        Date date = new Date(timeNum);
        System.out.println("这个session的最后访问时间是:"+date);

        long timeNum2 = session.getCreationTime();
        Date date2 = new Date(timeNum2);
        System.out.println("这个session的创建时间是:"+date2);

        //session
        SecUser userone = SessionUtil.getCurrentUser();
        if (userone == null) {
            return "not login";
        } else {
            return userone;
        }
    }
}
