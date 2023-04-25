package com.graduation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.graduation.exception.ServiceException;
import com.graduation.mapper.*;
import com.graduation.model.*;
import com.graduation.service.IQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.graduation.service.ITagService;
import com.graduation.service.IUserService;
import com.graduation.vo.HotQuestionVo;
import com.graduation.vo.QuestionVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private QuestionTagMapper questionTagMapper;
    @Resource
    private UserQuestionMapper userQuestionMapper;
    @Resource
    private UserCollectMapper userCollectMapper;
    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private ITagService tagService;
    @Resource
    private IUserService userService;


    @Override
    public PageInfo<Question> getMyQuestions(String username,Integer pageNum,Integer pageSize) {
        //根据用户名查询用户信息
        User user = userMapper.findUserByUsername(username);
        //根据当前登录用户id查询问题
        QueryWrapper<Question> query = new QueryWrapper<>();
        query.eq("user_id",user.getId());
        query.eq("delete_status",0);
        query.orderByDesc("createtime");
        //查询前进行分页设置
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questionsList = questionMapper.selectList(query);
        for (Question question : questionsList){
            List<Tag> tags = tagName2Tags(question.getTagNames());
            question.setTags(tags);
        }
        log.debug("当前用户问题数量: {}",questionsList.size());
//        System.out.println("当前问题："+questionsList);
        return new PageInfo<>(questionsList);
    }

    @Override
    public PageInfo<Question> getClassroomQuestions(String username,Integer pageNum,Integer pageSize) {
        User user = userMapper.findUserByUsername(username);
        PageHelper.startPage(pageNum, pageSize);
        List<Question> classroomQuestions = questionMapper.getClassroomQuestions(user.getClassroomId());
        for (Question question : classroomQuestions){
            List<Tag> tags = tagName2Tags(question.getTagNames());
            question.setTags(tags);
        }
        log.debug("当前用户同班级的问题数量: {}",classroomQuestions.size());
        return new PageInfo<>(classroomQuestions);
    }

    @Transactional
    @Override
    public void saveQuestion(QuestionVo questionVo, String username) {
        //根据用户名查询用户信息
        User user = userMapper.findUserByUsername(username);
        //根据学生选中的标签构建tag_names列的值
        StringBuilder builder = new StringBuilder();
        //{"java基础","javaSE","面试题"}
        for (String tagName : questionVo.getTagNames()){
            builder.append(tagName).append(",");
        }
        //"java基础,javaSE,面试题"
        String tagNames = builder.deleteCharAt(builder.length()-1).toString();
        //实例化Question对象并赋值
        Question question = new Question()
                .setTitle(questionVo.getTitle())
                .setContent(questionVo.getContent())
                .setUserNickName(user.getNickname())
                .setUserId(user.getId())
                .setCreatetime(LocalDateTime.now())
                .setStatus(0)
                .setPageViews(0)
                .setPublicStatus(0)
                .setDeleteStatus(0)
                .setTagNames(tagNames);
        //新增到数据库
        int num = questionMapper.insert(question);
        if(num!=1){
            throw new ServiceException("服务器忙!");
        }
        //新增Question和tag的关系
        //获取包含所有标签的Map
        Map<String, Tag> tagMap = tagService.getTagMap();
        for (String tagName : questionVo.getTagNames()){
            Tag t = tagMap.get(tagName);
            QuestionTag questionTag = new QuestionTag()
                    .setQuestionId(question.getId())
                    .setTagId(t.getId());
            num = questionTagMapper.insert(questionTag);
            if(num!=1){
                throw new ServiceException("服务器忙");
            }
            log.debug("新增了问题和标签的关系:{}",questionTag);
        }
        //新增User(教师)和Question的关系
        //获取包含所有讲师的Map
        Map<String, User> teacherMap = userService.getTeacherMap();
        for (String nickName : questionVo.getTeacherNicknames()){
            User teacher = teacherMap.get(nickName);
            UserQuestion userQuestion = new UserQuestion()
                    .setUserId(teacher.getId())
                    .setQuestionId(question.getId())
                    .setCreatetime(LocalDateTime.now());
            num = userQuestionMapper.insert(userQuestion);
            if(num!=1){
                throw new ServiceException("服务器忙");
            }
            log.debug("新增了讲师和问题的关系:{}",userQuestion);

            Notice notice = new Notice()
                    .setType(1)
                    .setQuestionId(question.getId())
                    .setCreatetime(question.getCreatetime())
                    .setUserId(question.getUserId())
                    .setReplyUserId(teacher.getId())
                    .setReadStatus(0);
            int noticeNum = noticeMapper.insert(notice);
            if(noticeNum!=1){
                throw new ServiceException("服务器忙");
            }
        }


    }

    @Override
    public Integer countQuestionsByUserId(Integer userId) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("delete_status",0);
        Integer count = questionMapper.selectCount(queryWrapper);
        return count;
    }

    @Override
    public Integer collectQuestionsByUserId(Integer userId) {
        QueryWrapper<UserCollect> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        Integer count = userCollectMapper.selectCount(query);
        return count;
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @Override
    public PageInfo<Question> getTeacherQuestions(String username, Integer pageNum, Integer pageSize) {
        User user = userMapper.findUserByUsername(username);
        //设置分页
        PageHelper.startPage(pageNum,pageSize);
        List<Question> teacherQuestions = questionMapper.findTeacherQuestions(user.getId(),user.getClassroomId());
        //对查询出的问题包含的标签进行赋值
        for (Question question : teacherQuestions){
            List<Tag> tags = tagName2Tags(question.getTagNames());
            question.setTags(tags);
        }
        if (user.getType() == 0){
            throw new ServiceException();
        }
        return new PageInfo<>(teacherQuestions);

    }

    @Override
    public Question getQuestionById(String username,Integer id) {
        User user = userMapper.findUserByUsername(username);
        Question question = questionMapper.selectById(id);
        QueryWrapper<Notice> query =  new QueryWrapper<>();
        query.eq("reply_user_id",user.getId());
        query.eq("question_id",id);
        Notice notice = noticeMapper.selectOne(query);
        if (notice != null){
            noticeMapper.updateNoticeReadStatus(user.getId(),id);
        }
        //给问题的标签赋值
        List<Tag> tags = tagName2Tags(question.getTagNames());
        question.setTags(tags);
        return question;
    }

    @Override
    public List<HotQuestionVo> getHotQuestion(String username) {
        User user = userMapper.findUserByUsername(username);
        List<Question> questions = questionMapper.getHotQuestionList(user.getClassroomId());
//        QueryWrapper<User> userQuery = new QueryWrapper<>();
//        QueryWrapper<Question> questionQuery =  new QueryWrapper<>();
//        userQuery.eq("classroom_id",user.getClassroomId());
//        QueryWrapper<User> id = userQuery.select("id");
//        List<User> users = userMapper.selectList(userQuery);
//        questionQuery.in("user_id",id);
//        questionQuery.orderByDesc("page_views").last("limit 10");
//        List<Question> questions = questionMapper.selectList(questionQuery);
        List<HotQuestionVo> hotQuestionVo = new ArrayList<>();
        for (Question question :questions){
            HotQuestionVo qv = new HotQuestionVo();
            Integer countAnswer = questionMapper.countAnswer(question.getId());
            qv.setId(question.getId());
            qv.setTitle(question.getTitle());
            qv.setStatus(question.getStatus());
            qv.setPageViews(question.getPageViews());
            qv.setCountAnswer(countAnswer);
            hotQuestionVo.add(qv);
        }
        return hotQuestionVo;
    }

    @Override
    public int deleteQuestionById(String username, Integer id) {
        User user = userMapper.findUserByUsername(username);
        Question question = questionMapper.selectById(id);
        if (user.getId() == question.getUserId()){
            int num = questionMapper.deleteQuestionById(id);
            return num;
        }else {
            throw new ServiceException("您无法删除此问题！");
        }
    }


    //根据tag_names的值获得一个对应的List<Tag>集合
    public List<Tag> tagName2Tags(String tagNames){
        //tagNames :  "Java基础,Java SE,面试题"
        // names:   {"Java基础","Java SE","面试题"}
        String[] names = tagNames.split(",");
        //获取包含所有标签的Map
        Map<String,Tag> tagMap = tagService.getTagMap();
        List<Tag> tags = new ArrayList<>();
        for (String name : names){
            tags.add(tagMap.get(name));
        }
        return tags;
    }
}
