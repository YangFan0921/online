package com.graduation.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("answer")
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 回答内容
     */
    @TableField("content")
    private String content;

    /**
     * 点赞数量
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 回答问题的用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 回答者用户名
     */
    @TableField("user_nick_name")
    private String userNickName;

    /**
     * 对应的问题id
     */
    @TableField("quest_id")
    private Integer questId;

    /**
     * 回答时间
     */
    @TableField("createtime")
    private LocalDateTime createtime;

    /**
     * 是否采纳
     */
    @TableField("accept_status")
    private Integer acceptStatus;

    /**
     * 当前回答的所有评论集合
     */
    @TableField(exist = false)
    private List<Comment> comments=new ArrayList<>();


}
