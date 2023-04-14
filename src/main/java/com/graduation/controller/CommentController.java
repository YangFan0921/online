package com.graduation.controller;


import com.graduation.exception.ServiceException;
import com.graduation.model.Comment;
import com.graduation.service.ICommentService;
import com.graduation.vo.CommentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
@RequestMapping("/comments")
public class CommentController {

    @Resource
    ICommentService commentService;

    @PostMapping("")
    public Comment postComment(@Validated CommentVo commentVo, BindingResult result, @AuthenticationPrincipal UserDetails user){
        log.debug("接收到评论信息:{}",commentVo);
        if(result.hasErrors()){
            String msg=result.getFieldError().getDefaultMessage();
            throw new ServiceException(msg);
        }
        Comment comment = commentService.saveComment(commentVo, user.getUsername());
        return comment;
    }

    @DeleteMapping("/{id}/delete")
    public String remove(@PathVariable Integer id,@AuthenticationPrincipal UserDetails user){
        boolean isDelete=commentService
                .removeCommentById(id,user.getUsername());
        if(isDelete){
            return "删除成功";
        }else{
            return "要删除的评论不存在";
        }
    }

    @GetMapping("/{id}/update")
    public Comment updateComment(@PathVariable Integer id,@Validated CommentVo commentVo,@AuthenticationPrincipal UserDetails user,BindingResult result){
        log.debug("修改内容:{}",commentVo);
        if(result.hasErrors()){
            String msg=result.getFieldError().getDefaultMessage();
            throw new ServiceException(msg);
        }
        Comment comment = commentService.updateComment(id, commentVo, user.getUsername());
        return comment;
    }

}
