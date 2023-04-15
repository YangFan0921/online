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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
        QueryWrapper<Notice> query = new QueryWrapper<>();
        query.eq("read_status",0);
        query.eq("reply_user_id",user.getId());
        Integer count = noticeMapper.selectCount(query);
        return count;
    }

    @Override
    public List<Question> tabContent(String username) {
        User user = userMapper.findUserByUsername(username);
//        QueryWrapper<Notice> query = new QueryWrapper<>();
//        query.eq("read_status",0);
//        query.eq("reply_user_id",user.getId());
//        List<Notice> noticeList =  noticeMapper.selectList(query);
        List<Notice> top10Notice = noticeMapper.getTop10Notice(user.getId());
        List<Question> questionList = new ArrayList<>();
        for (Notice notice : top10Notice) {
//            System.out.println(notice);
            Question question = questionMapper.selectById(notice.getQuestionId());
            questionList.add(question);
        }
        return questionList;
    }

    @Override
    public PageInfo<NoticeQuestionVo> getAllNotices(String username,Integer pageNum,Integer pageSize) {
        User user = userMapper.findUserByUsername(username);
        QueryWrapper<Notice> query = new QueryWrapper<>();
        query.eq("reply_user_id",user.getId());
        List<Notice> noticeList = noticeMapper.selectList(query);
        List<NoticeQuestionVo> voList =  new ArrayList<>();
        for (Notice notice : noticeList){
            NoticeQuestionVo vo = new NoticeQuestionVo()
                    .setId(notice.getQuestionId())
                    .setTitle(questionMapper.selectById(notice.getQuestionId()).getTitle())
                    .setCreatetime(notice.getCreatetime())
                    .setReadStatus(notice.getReadStatus())
                    .setNickname(questionMapper.selectById(notice.getQuestionId()).getUserNickName());
            voList.add(vo);
        }
        return  new PageInfo<>(voList);
    }
}
