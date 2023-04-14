package com.graduation.controller;


import com.graduation.exception.ServiceException;
import com.graduation.model.Answer;
import com.graduation.service.IAnswerService;
import com.graduation.vo.AnswerVo;
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
@RequestMapping("/answers")
public class AnswerController {

    @Resource
    IAnswerService answerService;

    @PostMapping("")
//    @PreAuthorize("hasRole('TEACHER')")
    public Answer addAnswer(@Validated AnswerVo answerVo, @AuthenticationPrincipal UserDetails user, BindingResult result){
        log.debug("收到信息:{}",answerVo);
        if(result.hasErrors()){
            String msg=result.getFieldError().getDefaultMessage();
            throw new ServiceException(msg);
        }
        Answer answer = answerService.saveAnswer(answerVo, user.getUsername());
        return answer;
    }

    @GetMapping("/question/{id}")
    public List<Answer> getQuestionAnswer(@PathVariable Integer id){
        if(id==null){
            throw new ServiceException("问题id不能为空");
        }
        List<Answer> answers = answerService.getAnswersByQuestionId(id);
        return answers;
    }


    @GetMapping("/{answerId}/solved")
    public String solved(@PathVariable Integer answerId,@AuthenticationPrincipal UserDetails user){
        Integer isAccept=answerService.accept(answerId, user.getUsername());
        if(isAccept == 2){
            return "你不是此问题的用户";

        }else if (isAccept == 1){
            return "采纳成功";
        }
        return "采纳失败,检查回答存在并且没有被采纳过";
    }

}
