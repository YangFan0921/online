package com.graduation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {
    //两个角色的常量,用于判断用户的角色
    public static final GrantedAuthority STUDENT = new SimpleGrantedAuthority("ROLE_STUDENT");
    public static final GrantedAuthority TEACHER = new SimpleGrantedAuthority("ROLE_TEACHER");

    @GetMapping(value = {"/","/index.html"})
    public String index(@AuthenticationPrincipal UserDetails user){
        //判断学生身份
        if (user.getAuthorities().contains(STUDENT)){
            return "redirect:/index_student.html";
            //判断教师身份
        }else if (user.getAuthorities().contains(TEACHER)){
            return "redirect:/index_teacher.html";
        }
        return "redirect:/login.html";
    }
}
