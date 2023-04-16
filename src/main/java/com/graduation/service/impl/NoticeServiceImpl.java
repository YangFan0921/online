package com.graduation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.graduation.exception.ServiceException;
import com.graduation.mapper.QuestionMapper;
import com.graduation.mapper.UserMapper;
import com.graduation.model.Notice;
import com.graduation.mapper.NoticeMapper;
import com.graduation.model.Question;
import com.graduation.model.User;
import com.graduation.service.INoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.graduation.vo.NoticeQuestionVo;
import com.graduation.vo.TaskVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private QuestionMapper questionMapper;


    @Override
    public Integer noticeCounts(String username) {
        User user = userMapper.findUserByUsername(username);
        Integer unreadCount = noticeMapper.getUnreadCount(user.getId());
        return unreadCount;
    }

    @Override
    public List<NoticeQuestionVo> tabContent(String username) {
        User user = userMapper.findUserByUsername(username);
        List<NoticeQuestionVo> top10Notice = noticeMapper.getTop10Notice(user.getId());
        return top10Notice;
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Override
    public PageInfo<NoticeQuestionVo> getAllNotices(String username,Integer pageNum,Integer pageSize) {
        User user = userMapper.findUserByUsername(username);
        List<NoticeQuestionVo> allNotice = noticeMapper.getAllNotice(user.getId());
        if (user.getType() == 0){
            throw new ServiceException();
        }
        return new PageInfo<>(allNotice);
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Override
    public PageInfo<TaskVo> getNoAnswerTasks(String username, Integer pageNum, Integer pageSize) {
        User user = userMapper.findUserByUsername(username);
        List<TaskVo> taskVos = noticeMapper.getNoAnswerTasks(user.getId());
        for (TaskVo vo : taskVos){
            LocalDateTime createtime = vo.getCreatetime();
            String date= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(createtime);
            vo.setCreatedate(date);
        }
        if (user.getType() == 0){
            throw new ServiceException();
        }
        return new PageInfo<>(taskVos);
    }

    @Override
    public PageInfo<TaskVo> getUnSolveTasks(String username, Integer pageNum, Integer pageSize) {
        User user = userMapper.findUserByUsername(username);
        List<TaskVo> taskVos =  noticeMapper.getUnSolveTasks(user.getId());
        for (TaskVo task : taskVos){
            LocalDateTime createtime = task.getCreatetime();
            String date= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(createtime);
            task.setCreatedate(date);
        }
        return new PageInfo<>(taskVos);
    }

    @Override
    public PageInfo<TaskVo> getSolvedTasks(String username, Integer pageNum, Integer pageSize) {
        User user = userMapper.findUserByUsername(username);
        List<TaskVo> taskVos =  noticeMapper.getSolvedTasks(user.getId());
        for (TaskVo task : taskVos){
            LocalDateTime createtime = task.getCreatetime();
            String date= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(createtime);
            task.setCreatedate(date);
        }
        return new PageInfo<>(taskVos);
    }
}
