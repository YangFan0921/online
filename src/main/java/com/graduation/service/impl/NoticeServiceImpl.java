package com.graduation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
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

    @Override
    public PageInfo<NoticeQuestionVo> getAllNotices(String username,Integer pageNum,Integer pageSize) {
        User user = userMapper.findUserByUsername(username);
        List<NoticeQuestionVo> allNotice = noticeMapper.getAllNotice(user.getId());
        return new PageInfo<>(allNotice);
    }

    @Override
    public PageInfo<TaskVo> getAllTasks(String username, Integer pageNum, Integer pageSize) {
        User user = userMapper.findUserByUsername(username);
        List<TaskVo> taskVos = noticeMapper.getMyTask(user.getId());
        for (TaskVo vo : taskVos){
            LocalDateTime createtime = vo.getCreatetime();
            String date= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(createtime);
            vo.setCreatedate(date);
        }
        return new PageInfo<>(taskVos);
    }
}
