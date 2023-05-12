package com.graduation.mapper;

import com.graduation.model.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    //根据评论id修改评论内容
    @Update("update comment set content=#{content} where id=#{id}")
    Integer updateCommentContentById(@Param("content") String content, @Param("id") Long id);

}
