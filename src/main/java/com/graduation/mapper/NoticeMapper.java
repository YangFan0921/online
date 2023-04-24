package com.graduation.mapper;

import com.graduation.model.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.graduation.model.Question;
import com.graduation.vo.NoticeQuestionVo;
import com.graduation.vo.TaskVo;
import org.apache.ibatis.annotations.Insert;
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
public interface NoticeMapper extends BaseMapper<Notice> {

    @Update("update notice set read_status=1 where reply_user_id=#{reply_user_id} and question_id=#{question_id}")
    Integer updateNoticeReadStatus(@Param("reply_user_id")Integer reply_user_id, @Param("question_id") Integer question_id);

    @Select("SELECT COUNT(1) from question q " +
            "left JOIN notice n on q.id=n.question_id " +
            "where n.reply_user_id=#{reply_user_id} and n.read_status=0 and q.delete_status=0;")
    Integer getUnreadCount(@Param("reply_user_id") Integer reply_user_id);

    @Select("select q.id,q.title,q.createtime,q.user_nick_name nickname,n.read_status from question q " +
            "LEFT JOIN notice n on q.id=n.question_id " +
            "where n.reply_user_id=#{reply_user_id} and n.read_status=0 and q.delete_status=0 " +
            "order by q.createtime desc limit 0,10")
    List<NoticeQuestionVo> getTop10Notice(@Param("reply_user_id") Integer reply_user_id);

    @Select("SELECT DISTINCT q.id,q.title,q.createtime,q.user_nick_name nickname,n.read_status from question q " +
            "LEFT JOIN notice n on q.id=n.question_id " +
            "where n.reply_user_id=#{reply_user_id}  and q.delete_status=0")
    List<NoticeQuestionVo> getAllNotice(@Param("reply_user_id") Integer reply_user_id);

    @Select("select DISTINCT n.question_id,u.nickname,q.createtime,q.title,q.status,q.page_views pageView,q.tag_names tagNames from notice n " +
            "left join question q on n.question_id=q.id " +
            "left join user u on n.user_id=u.id " +
            "where n.reply_user_id=#{reply_user_id} and status=0 and q.delete_status=0 " +
            "ORDER BY q.createtime desc ")
    List<TaskVo> getNoAnswerTasks(@Param("reply_user_id") Integer reply_user_id);

    @Select("select DISTINCT n.question_id,u.nickname,q.createtime,q.title,q.status,q.page_views pageView,q.tag_names tagNames from notice n " +
            "left join question q on n.question_id=q.id " +
            "left join user u on n.user_id=u.id " +
            "where n.reply_user_id=#{reply_user_id} and status=1 and q.delete_status=0 " +
            "ORDER BY q.createtime desc ")
    List<TaskVo> getUnSolveTasks(@Param(value = "reply_user_id") Integer reply_user_id);

    @Select("select DISTINCT n.question_id,u.nickname,q.createtime,q.title,q.status,q.page_views pageView,q.tag_names tagNames from notice n " +
            "left join question q on n.question_id=q.id " +
            "left join user u on n.user_id=u.id " +
            "where n.reply_user_id=#{reply_user_id} and status=2 and q.delete_status=0 " +
            "ORDER BY q.createtime desc ")
    List<TaskVo> getSolvedTasks(@Param(value = "reply_user_id") Integer reply_user_id);


}
