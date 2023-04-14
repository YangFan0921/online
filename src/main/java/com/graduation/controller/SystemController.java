package com.graduation.controller;

import com.graduation.exception.ServiceException;
import com.graduation.service.IUserService;
import com.graduation.vo.RegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
public class SystemController {

    @Resource
    IUserService userService;

    @PostMapping("/register")
    public String registerStudent(@Validated RegisterVo registerVo, BindingResult result){
        if (result.hasErrors()){
            String msg = result.getFieldError().getDefaultMessage();
            return msg;
        }
        log.debug("收到学生注册信息: {}",registerVo);
        try {
            userService.registerStudent(registerVo);
            return "注册成功";
        }catch (ServiceException e){
            log.error("注册失败",e);
            return e.getMessage();
        }
    }

    //文件上传
    @Value("${file.upload.path}")
    private File imageDirPath;

    @PostMapping("/upload/file")
    public String uploadFile(MultipartFile imageFile) throws IOException {
        String fileName = imageFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        File uploadParentFile = new File(imageDirPath+"");
        uploadParentFile.mkdirs();
        fileName = UUID.randomUUID()+suffix;
        File uploadFile = new File(uploadParentFile,fileName);
        String url = null;
        try {
            imageFile.transferTo(uploadFile);
            url = "http://localhost:8080/upload/" + fileName;
//            url = imageDirPath + "\\" + fileName;
            System.out.println("url=====>"+url);
            log.debug("保存的实际路径为:{}",uploadFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    @PostMapping("/upload/avatar")
    public String uploadAvatar(MultipartFile file) throws IOException {
        String url = userService.uploadAvatar(file);
        return url;
    }


}
