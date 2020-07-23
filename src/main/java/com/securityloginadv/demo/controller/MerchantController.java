package com.securityloginadv.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/merchant")
public class MerchantController {

    //商户首页
    @RequestMapping("/home")
    @ResponseBody
    public String home(){
        return "hello merchant home";
    }
}
