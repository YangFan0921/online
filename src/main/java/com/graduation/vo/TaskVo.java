package com.graduation.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Accessors(chain = true)
public class TaskVo implements Serializable {

    private Integer questionId;

    private String title;

    private String tagNames;

    private String nickname;

    private LocalDateTime createtime;

    private String createdate;

    private Integer status;

    private Integer pageView;




}
