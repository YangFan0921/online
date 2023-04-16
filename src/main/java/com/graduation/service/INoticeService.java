package com.graduation.service;

import com.github.pagehelper.PageInfo;
import com.graduation.model.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.graduation.model.Question;
import com.graduation.vo.NoticeQuestionVo;
import com.graduation.vo.TaskVo;

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

    List<NoticeQuestionVo> tabContent(String username);

    //查询所有通知列表
    PageInfo<NoticeQuestionVo>  getAllNotices(String username, Integer pageNum, Integer pageSize);

    //教师任务列表
    PageInfo<TaskVo>  getAllTasks(String username, Integer pageNum, Integer pageSize);

}
