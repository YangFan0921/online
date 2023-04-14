package com.graduation.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Data
@Accessors(chain = true)
public class UserInfoVo implements Serializable {

    private String avatarUrl;

    @NotBlank(message = "昵称不能为空")
    @Pattern(regexp = "^.{2,20}$",message = "昵称是2~20个字符")
    private String nickname;

    private String birthday;

    private String sex;

    private String selfIntroduction;

}
