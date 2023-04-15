package com.graduation.controller;


import com.github.pagehelper.PageInfo;
import com.graduation.model.Question;
import com.graduation.service.INoticeService;
import com.graduation.vo.NoticeQuestionVo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    INoticeService noticeService;

    @GetMapping("/counts")
    public Integer counts(@AuthenticationPrincipal UserDetails user){
       return noticeService.noticeCounts(user.getUsername());
    }

    @GetMapping("/tabContent")
    public List<Question> tabContent(@AuthenticationPrincipal UserDetails user){
        return  noticeService.tabContent(user.getUsername());
    }

    @GetMapping("")
    public PageInfo<NoticeQuestionVo> allNotice(@AuthenticationPrincipal UserDetails user,Integer pageNum) {
        if (pageNum==null){
            pageNum=1;
        }
        Integer pageSize=8;
        PageInfo<NoticeQuestionVo> pageInfo = noticeService.getAllNotices(user.getUsername(), pageNum, pageSize);
        return pageInfo;
    }

}
