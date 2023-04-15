package com.graduation.controller;


import com.alibaba.druid.sql.visitor.functions.Bin;
import com.github.pagehelper.PageInfo;
import com.graduation.exception.ServiceException;
import com.graduation.model.Question;
import com.graduation.service.IQuestionService;
import com.graduation.vo.HotQuestionVo;
import com.graduation.vo.QuestionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/questions")
public class QuestionController {

    @Resource
    IQuestionService questionService;

    @GetMapping("")
    public PageInfo<Question> getClassroomQuestions(@AuthenticationPrincipal UserDetails user,Integer pageNum){
        if (pageNum==null){
            pageNum=1;
        }
        Integer pageSize=8;
        PageInfo<Question> pageInfo  = questionService.getClassroomQuestions(user.getUsername(),pageNum,pageSize);
        return pageInfo;
    }

    @GetMapping("/my")
    public PageInfo<Question> myQuestions(@AuthenticationPrincipal UserDetails user,Integer pageNum){
        if(pageNum==null)
            pageNum=1;
        Integer pageSize=8;
        PageInfo<Question> pageInfo = questionService.getMyQuestions(user.getUsername(),pageNum,pageSize);
        return pageInfo;
    }

    @PostMapping("")
    public String createQuestion(@Validated QuestionVo questionVo, BindingResult result,@AuthenticationPrincipal UserDetails user){
        log.debug("接收到问题内容:{}",questionVo);
        if (result.hasErrors()){
            String msg = result.getFieldError().getDefaultMessage();
            return msg;
        }
        try {
            questionService.saveQuestion(questionVo,user.getUsername());
            return "问题发布成功";
        }catch (ServiceException e){
            log.error("问题发布失败",e);
            return e.getMessage();
        }
    }

    @GetMapping("/teacher")
    //指定登录用户必须包含ROLE_TEACHER这个身份
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public PageInfo teacher(@AuthenticationPrincipal UserDetails user,Integer pageNum){
        if (pageNum == null){
            pageNum =1;
        }
        Integer pageSize = 8;
        PageInfo<Question> pageInfo = questionService.getTeacherQuestions(user.getUsername(), pageNum, pageSize);
        return pageInfo;
    }

    @GetMapping("/{id}")
    public Question question(@AuthenticationPrincipal UserDetails user,@PathVariable Integer id){
        if (id == null){
            throw new ServiceException("问题ID不能为空");
        }
        Question question = questionService.getQuestionById(user.getUsername(), id);
        return question;
    }

    @GetMapping("/hotQuestions")
    public List<HotQuestionVo> getTop10(@AuthenticationPrincipal UserDetails user){
        return questionService.getHotQuestion(user.getUsername());
    }

}
