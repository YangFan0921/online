package com.graduation.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class HotQuestionVo implements Serializable {
    //问题id
    private Integer id;
    //问题标题
    private String title;
    //问题状态
    private Integer status;
    //浏览量
    private Integer pageViews;
    //回答数
    private Integer countAnswer;

}
