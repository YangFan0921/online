package com.graduation.controller;


import com.graduation.model.User;
import com.graduation.service.IUserService;
import com.graduation.vo.ResetPasswordByPhoneVo;
import com.graduation.vo.ResetPasswordVo;
import com.graduation.vo.UserInfoVo;
import com.graduation.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    @Resource
    IUserService userService;

    @GetMapping("/master")
    public List<User> teachers(){
        return userService.getTeachers();
    }

    @GetMapping("/me")
    public UserVo getUserVo(@AuthenticationPrincipal UserDetails user){
        UserVo userVo = userService.getCurrentUserVo(user.getUsername());
        return userVo;
    }

    @GetMapping("/myInfo")
    public User getMyInfo(@AuthenticationPrincipal UserDetails user){
        User myInfo = userService.getMyInfo(user.getUsername());
        return myInfo;
    }

    @PostMapping("/updateMyInfo")
    public String updateMyInfo(@AuthenticationPrincipal UserDetails user, @Validated UserInfoVo userInfoVo, BindingResult result){
        if (result.hasErrors()){
            String msg = result.getFieldError().getDefaultMessage();
            log.debug(msg);
            return msg;
        }
        log.debug("收到学生修改信息: {}",userInfoVo);
        String s = userService.updateMyInfo(user.getUsername(), userInfoVo);
        return s;
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@AuthenticationPrincipal UserDetails user, @Validated ResetPasswordVo resetPassword, BindingResult result){
        if (result.hasErrors()){
            String msg = result.getFieldError().getDefaultMessage();
            log.debug(msg);
            return msg;
        }
        log.debug("收到学生修改密码: {}",resetPassword);
        String s = userService.ResetPassword(user.getUsername(), resetPassword);
        return s;
    }

    @PostMapping("/resetPasswordByPhone")
    public String resetPasswordByPhone(@Validated ResetPasswordByPhoneVo resetPasswordByPhoneVo,BindingResult result){
        if (result.hasErrors()){
            String msg = result.getFieldError().getDefaultMessage();
            log.debug("验证信息有误: {}",msg);
            return msg;
        }
        log.debug("收到学生修改密码: {}",resetPasswordByPhoneVo);
        String s = userService.resetPasswordByPhone(resetPasswordByPhoneVo);
        return s;
    }
}
