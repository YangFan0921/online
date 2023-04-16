package com.graduation.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TaskVo implements Serializable {

    private Integer id;

    private Integer questionId;

    private String title;

    private String[] tags;

    private String nickname;

    private LocalDateTime createtime;

    private Integer status;

    private Integer pageView;




}
