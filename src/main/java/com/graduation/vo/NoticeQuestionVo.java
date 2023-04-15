package com.graduation.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class NoticeQuestionVo implements Serializable {
    //问题id
    private Integer id;
    //用户昵称
    private String nickname;
    //问题标题
    private String title;
    //查看状态
    private Integer readStatus;
    //创建时间
    private LocalDateTime createtime;

}
