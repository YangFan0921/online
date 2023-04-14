package com.graduation.service;

import com.graduation.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.graduation.vo.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
public interface IUserService extends IService<User> {

    //学生注册
    void registerStudent(RegisterVo registerVo);
    //查询所有教师的方法
    List<User> getTeachers();
    //查询所有教师Map集合的方法
    Map<String,User> getTeacherMap();

    //查询用户信息面板信息
    UserVo getCurrentUserVo(String username);

    //查询个人信息
    User getMyInfo(String username);

    //修改个人信息
    String updateMyInfo(String username, UserInfoVo userInfoVo);

    //修改个人头像
    String uploadAvatar(MultipartFile file) throws IOException;

    //根据密码修改新密码
    String ResetPassword(String username, ResetPasswordVo resetPasswordVo);

    //根据用户名(手机号)修改新密码
    String resetPasswordByPhone( ResetPasswordByPhoneVo resetPasswordByPhoneVo);


}
