package com.graduation.service;

import com.github.pagehelper.PageInfo;
import com.graduation.model.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.graduation.vo.HotQuestionVo;
import com.graduation.vo.QuestionVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
public interface IQuestionService extends IService<Question> {

    // 查询当前登录学生的问题
    PageInfo<Question> getMyQuestions(String username,Integer pageNum,Integer pageSize);
    //查询同班级的学生的问题
    PageInfo<Question> getClassroomQuestions(String username,Integer pageNum,Integer pageSize);
    //新增问题的
    void saveQuestion(QuestionVo questionVo, String username);

    //根据用户id查询当前用户的问题数
    Integer countQuestionsByUserId(Integer userId);

    //根据用户id查询当前用户的收藏数
    Integer collectQuestionsByUserId(Integer userId);

    // 查询登录讲师的任务列表
    PageInfo<Question> getTeacherQuestions(
            String username,Integer pageNum,Integer pageSize);

    // 根据问题id查询问题详情
    Question getQuestionById(String username,Integer id);

    //根据用户的教室ID查询热点问题列表
    List<HotQuestionVo> getHotQuestion(String username);


}
