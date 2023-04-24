package com.graduation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.graduation.controller.SystemController;
import com.graduation.exception.ServiceException;
import com.graduation.mapper.*;
import com.graduation.model.*;
import com.graduation.service.IQuestionService;
import com.graduation.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.graduation.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    //教师的临时缓存
    private List<User> teachers=new CopyOnWriteArrayList<>();
    private Map<String,User> teacherMap=new ConcurrentHashMap<>();

    @Resource
    UserMapper userMapper;
    @Resource
    ClassroomMapper classroomMapper;
    @Resource
    UserRoleMapper userRoleMapper;
    @Resource
    QuestionMapper questionMapper;
    @Resource
    AnswerMapper answerMapper;
    @Resource
    CommentMapper commentMapper;
    @Resource
    IQuestionService questionService;

    private PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void registerStudent(RegisterVo registerVo) {
        //验证邀请码
        QueryWrapper<Classroom> query=new QueryWrapper<>();
        query.eq("invite_code",registerVo.getInviteCode());
        Classroom classroom=classroomMapper.selectOne(query);
        if(classroom==null){
            throw new ServiceException("邀请码不正确!");
        }
        //验证手机号(用户名)
        User user=userMapper.findUserByUsername(registerVo.getPhone());
        if(user!=null){
            throw new ServiceException("手机号已经被注册过!");
        }
        User u = new User();
        //密码加密
        String pwd="{bcrypt}"+encoder.encode(registerVo.getPassword());
        //User赋值
        u.setUsername(registerVo.getPhone())
                .setNickname(registerVo.getNickname())
                .setPassword(pwd)
                .setClassroomId(classroom.getId())
                .setCreatetime(LocalDateTime.now())
                .setEnabled(1)
                .setLocked(0)
                .setType(0);
        //新增到数据库
        int num = userMapper.insert(u);
        if (num != 1){
            throw new ServiceException("服务器忙,请稍后再试");
        }
        //新增用户和角色关系表的数据
        UserRole userRole = new UserRole();
        userRole.setUserId(u.getId());
        userRole.setRoleId(2);
        num = userRoleMapper.insert(userRole);
        if (num != 1){
            throw new ServiceException("服务器忙,请稍后再试");
        }

    }

    @Override
    public List<User> getTeachers() {
        if (teachers.isEmpty()){
            synchronized (teachers){
                if (teachers.isEmpty()){
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("type",1);
                    teachers = userMapper.selectList(queryWrapper);
                    for (User user:teachers){
                        teacherMap.put(user.getNickname(),user);
                    }
                }

            }
        }
        return teachers;
    }

    @Override
    public Map<String, User> getTeacherMap() {
        if (teachers.isEmpty()){
            getTeachers();
        }
        return teacherMap;
    }

    @Override
    public UserVo getCurrentUserVo(String username) {
        UserVo userVo = userMapper.findUserVoByUsername(username);
        //按用户id查询这个用户的提问数
        int questions = questionService.countQuestionsByUserId(userVo.getId());
        //收藏数
        int collects = questionService.collectQuestionsByUserId(userVo.getId());
        userVo.setQuestions(questions);
        userVo.setCollections(collects);
        return userVo;
    }

    @Override
    public User getMyInfo(String username) {
        User user = userMapper.findUserByUsername(username);
        return user;
    }

    @Value("${file.upload.path}")
    private File imageDirPath;
    String url = null;
    @Override
    public String uploadAvatar(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        File uploadParentFile = new File(imageDirPath + "");
        uploadParentFile.mkdirs();
        fileName = UUID.randomUUID() + suffix;
        File uploadFile = new File(uploadParentFile, fileName);
//        String url = null;
        try {
            file.transferTo(uploadFile);
            url = "http://localhost:8080/upload/" + fileName;
//            url = imageDirPath + "\\" + fileName;
            System.out.println("url=====>" + url);
            log.debug("保存的实际路径为:{}", uploadFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    public String ResetPassword(String username, ResetPasswordVo resetPasswordVo) {
        User user = userMapper.findUserByUsername(username);
        String inputPassword = resetPasswordVo.getOldPassword();
        String sqlPassword = user.getPassword();
        String userPassword = sqlPassword.replace("{bcrypt}","");
        String newPassword = resetPasswordVo.getNewPassword();
        boolean matches = encoder.matches(inputPassword, userPassword);
        System.out.println("matches================>"+matches);
        if (matches){
            String pwd="{bcrypt}"+encoder.encode(newPassword);
            user.setPassword(pwd);
            int num = userMapper.updateById(user);
            if (num!=1){
                throw new ServiceException("服务器忙,请稍后再试");
            }
            return "修改成功";
        }else {
            return "原密码错误";
        }
    }

    @Override
    public String resetPasswordByPhone(ResetPasswordByPhoneVo resetPasswordByPhoneVo) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username",resetPasswordByPhoneVo.getUsername());
        User user = userMapper.selectOne(query);
        if (user == null){
            return "此用户不存在";
        }
        String password = resetPasswordByPhoneVo.getPassword();
        String pwd = "{bcrypt}"+encoder.encode(password);
        user.setPassword(pwd);
        int num = userMapper.updateById(user);
        if (num!=1){
            throw new ServiceException("服务器忙,请稍后再试");
        }
        return "修改成功";
    }

    @Override
    public String updateMyInfo(String username,UserInfoVo userInfoVo) {
        User user = userMapper.findUserByUsername(username);
        int num;
        LocalDate birthday = LocalDate.parse(userInfoVo.getBirthday());
        String avatarUrl = url;
        user.setAvatarUrl(avatarUrl)
                .setNickname(userInfoVo.getNickname())
                .setBirthday(birthday)
                .setSelfIntroduction(userInfoVo.getSelfIntroduction())
                .setSex(userInfoVo.getSex());
        num = userMapper.updateById(user);

        QueryWrapper<Question> questionQueryWrapper =  new QueryWrapper<>();
        questionQueryWrapper.eq("user_id",user.getId());
        List<Question> question = questionMapper.selectList(questionQueryWrapper);
        if (question!=null){
            for (Question q:question){
                q.setUserNickName(userInfoVo.getNickname());
                num = questionMapper.updateById(q);
            }
        }

        QueryWrapper<Answer> answerQueryWrapper =  new QueryWrapper<>();
        answerQueryWrapper.eq("user_id",user.getId());
        List<Answer> answer = answerMapper.selectList(answerQueryWrapper);
        if (answer!=null){
            for (Answer a:answer){
                a.setUserNickName(userInfoVo.getNickname());
                num = answerMapper.updateById(a);
            }
        }

        QueryWrapper<Comment> commentQueryWrapper =  new QueryWrapper<>();
        commentQueryWrapper.eq("user_id",user.getId());
        List<Comment> comment = commentMapper.selectList(commentQueryWrapper);
        if (comment!=null){
            for (Comment c:comment){
                c.setUserNickName(userInfoVo.getNickname());
                num = commentMapper.updateById(c);
            }

        }

        if (num!=1){
            throw new ServiceException("服务器忙,请稍后再试");
        }
        return "修改成功";
    }

    //计时器 30分钟清空一次教师缓存
    private Timer timer = new Timer();
    {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (teachers){
                    synchronized (teacherMap){
                        teachers.clear();
                        teacherMap.clear();
                    }
                }
                System.out.println("教师缓存已清空");
            }
        },30*60*1000,30*60*1000);
    }
}
