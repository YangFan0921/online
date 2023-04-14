package com.graduation.mapper;

import com.graduation.model.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
public interface QuestionMapper extends BaseMapper<Question> {

    @Select("select q.* " +
            "from user u " +
            "right JOIN question q on q.user_nick_name=u.nickname " +
            "where classroom_id=#{classroomId} " +
            "ORDER BY q.createtime DESC ")
    List<Question> getClassroomQuestions(Integer classroomId);

    @Select("SELECT q.* FROM" +
            "    question q" +
            " LEFT JOIN" +
            "    user_question uq" +
            " ON" +
            "    q.id=uq.question_id" +
            " WHERE" +
            "    q.user_id=#{userId} OR uq.user_id=#{userId}" +
            " ORDER BY q.createtime DESC ")
    List<Question> findTeacherQuestions(Integer userId);

    @Update("update question set status=#{status} where id=#{questionId}")
    Integer updateStatus(@Param("status")Integer status, @Param("questionId")Integer questionId);


}
