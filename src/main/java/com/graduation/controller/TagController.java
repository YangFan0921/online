package com.graduation.controller;


import com.graduation.model.Tag;
import com.graduation.service.ITagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    @Resource
    ITagService tagService;

    @GetMapping("")
    public List<Tag> tags(){
        return tagService.getTags();
    }

}
