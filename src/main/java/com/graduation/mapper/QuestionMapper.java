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
            "where classroom_id=#{classroomId} and q.delete_status=0 " +
            "ORDER BY q.createtime DESC ")
    List<Question> getClassroomQuestions(Integer classroomId);

//    教师提问 或者 提问教师的问题
//    @Select("SELECT q.* FROM question q " +
//            " LEFT JOIN user_question uq " +
//            " ON" +
//            "    q.id=uq.question_id" +
//            " WHERE" +
//            "    q.user_id=#{userId} OR uq.user_id=#{userId}" +
//            " ORDER BY q.createtime DESC ")
//    或者 同班级的问题
    @Select("SELECT distinct q.* FROM question q " +
            "LEFT JOIN user_question uq ON q.id=uq.question_id " +
            "LEFT JOIN user u on q.user_nick_name=u.nickname " +
            "WHERE q.delete_status=0 and (q.user_id=#{userId}  OR uq.user_id=#{userId}  OR classroom_id=#{classroomId}) ORDER BY q.createtime DESC")
    List<Question> findTeacherQuestions(@Param("userId") Integer userId,@Param("classroomId") Integer classroomId);

    @Update("update question set status=#{status} where id=#{questionId}")
    Integer updateStatus(@Param("status")Integer status, @Param("questionId")Integer questionId);

    @Select("select question.* from question where user_id in (select id from user where classroom_id=#{classroomId})  order by page_views desc limit 0,10")
    List<Question> getHotQuestionList(Integer classroomId);

    @Select("select count(1) from answer where quest_id=#{questId}")
    Integer countAnswer(Integer questId);


    @Update("update question set delete_status=1 where  id=#{questionId}")
    Integer deleteQuestionById(@Param("questionId") Integer questionId);
}
