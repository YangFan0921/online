package com.graduation.service.impl;

import com.graduation.exception.ServiceException;
import com.graduation.mapper.UserMapper;
import com.graduation.model.Comment;
import com.graduation.mapper.CommentMapper;
import com.graduation.model.User;
import com.graduation.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.graduation.vo.CommentVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    UserMapper userMapper;
    @Resource
    CommentMapper commentMapper;

    @Transactional
    @Override
    public Comment saveComment(CommentVo commentVo, String username) {
        User user = userMapper.findUserByUsername(username);
        Comment comment = new Comment()
                .setUserId(user.getId())
                .setContent(commentVo.getContent())
                .setAnswerId(commentVo.getAnswerId())
                .setUserNickName(user.getNickname())
                .setCreatetime(LocalDateTime.now());
        int num = commentMapper.insert(comment);
        if(num!=1){
            throw new ServiceException("服务器忙");
        }
        return comment;

    }

    @Override
    public boolean removeCommentById(Integer id, String username) {
        User user=userMapper.findUserByUsername(username);
        //判断是不是讲师
        if(user.getType()==1){
            int num=commentMapper.deleteById(id);
            return num==1;
        }
        //获得当前要删除的评论的详细信息
        Comment comment=commentMapper.selectById(id);
        //判断当前登录用户是不是评论的发布者
        if(user.getId()==comment.getUserId()){
            int num=commentMapper.deleteById(id);
            return num == 1;
        }
        //上面两个if都不进,表示当前用户既不是讲师也不是评论的发布者
        throw new ServiceException("您不能删除这个评论!");
    }

    @Transactional
    @Override
    public Comment updateComment(Integer commentId, CommentVo commentVo, String username) {
        User user=userMapper.findUserByUsername(username);
        Comment comment = commentMapper.selectById(commentId);
        if (user.getType()==1 || user.getId()==comment.getUserId()){
            int num = commentMapper.updateCommentContentById(commentVo.getContent(), commentId);
            comment.setContent(commentVo.getContent());
            if(num!=1){
                throw new ServiceException("服务器忙");
            }
            return comment;
        }
        throw new ServiceException("您不能修改这个评论");
    }
}
