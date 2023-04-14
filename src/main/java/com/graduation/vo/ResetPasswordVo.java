package com.graduation.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
public class ResetPasswordVo {

    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^\\w{6,20}$",message = "密码是6~20位数字字母'_'")
    private String newPassword;

    @NotBlank(message = "密码确认不能为空")
    private String confirmPassword;

}
