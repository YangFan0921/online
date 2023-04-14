package com.graduation.mapper;

import com.graduation.model.Answer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
@Repository
public interface AnswerMapper extends BaseMapper<Answer> {

    // 按问题id查询包含所有评论的回答list
    // 方法名和xml文件对应
    List<Answer> findAnswersWithCommentsByQuestionId(Integer id);

    //修改指定id的回答的采纳状态
    @Update("update answer set accept_status=#{acceptStatus} where id=#{answerId}")
    Integer updateAcceptStatus(@Param("acceptStatus")Integer acceptStatus, @Param("answerId") Integer answerId);

}
