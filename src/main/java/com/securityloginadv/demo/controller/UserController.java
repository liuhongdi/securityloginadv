package com.securityloginadv.demo.controller;

import com.securityloginadv.demo.security.SecUser;
import com.securityloginadv.demo.util.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/home")
    @ResponseBody
    public String home(){
        return "hello user home";
    }

    //修改密码页面
    @GetMapping("/setpass")
    public String setPass(Model model) {
        SecUser currentUser = SessionUtil.getCurrentUser();
        model.addAttribute("current_user",currentUser.getUsername());
        return "user/setpass";
    }

}
