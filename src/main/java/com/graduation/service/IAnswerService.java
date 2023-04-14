package com.graduation.service;

import com.graduation.model.Answer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.graduation.vo.AnswerVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
public interface IAnswerService extends IService<Answer> {

    // 新增回答
    // 返回值Answer,因为返回值要直接显示在页面上
    Answer saveAnswer(AnswerVo answerVo, String username);

    // 按问题id查询回答
    List<Answer> getAnswersByQuestionId(Integer questionId);

    // 采纳答案
    // 如果想做的严谨一些,可以传入当前登录用户的username
    // 来判断是不是问题的提问者在采纳答案
    int accept(Integer answerId,String username);

}
