package com.graduation.service;

import com.github.pagehelper.PageInfo;
import com.graduation.model.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.graduation.model.Question;
import com.graduation.vo.NoticeQuestionVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
public interface INoticeService extends IService<Notice> {

    Integer noticeCounts(String username);

    List<Question> tabContent(String username);

    //查询所有通知列表
    PageInfo<NoticeQuestionVo>  getAllNotices(String username, Integer pageNum, Integer pageSize);

}
