package com.securityloginadv.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController {

    //login页面
    @RequestMapping("/login")
    public String login(ModelMap modelMap){
        return "login/login";
    }

}
