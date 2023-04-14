package com.graduation.service;

import com.graduation.model.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.graduation.vo.CommentVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
public interface ICommentService extends IService<Comment> {
    //新增评论
    Comment saveComment(CommentVo commentVo, String username);

    //按评论id删除评论的方法
    boolean removeCommentById(Integer id,String username);

    //按评论id修改评论内容
    Comment updateComment(Integer commentId,CommentVo commentVo, String username);

}
