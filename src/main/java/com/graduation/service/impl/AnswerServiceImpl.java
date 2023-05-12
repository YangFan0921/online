package com.graduation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.graduation.exception.ServiceException;
import com.graduation.mapper.QuestionMapper;
import com.graduation.mapper.UserMapper;
import com.graduation.model.Answer;
import com.graduation.mapper.AnswerMapper;
import com.graduation.model.Question;
import com.graduation.model.User;
import com.graduation.service.IAnswerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.graduation.vo.AnswerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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
@Slf4j
public class AnswerServiceImpl extends ServiceImpl<AnswerMapper, Answer> implements IAnswerService {

    @Resource
    UserMapper userMapper;
    @Resource
    AnswerMapper answerMapper;
    @Resource
    QuestionMapper questionMapper;

    @Transactional
    @Override
    public Answer saveAnswer(AnswerVo answerVo, String username) {
        User user = userMapper.findUserByUsername(username);
        Answer answer = new Answer()
                .setContent(answerVo.getContent())
                .setUserNickName(user.getNickname())
                .setQuestId(answerVo.getQuestionId())
                .setCreatetime(LocalDateTime.now())
                .setUserId(user.getId())
                .setLikeCount(0)
                .setAcceptStatus(0);
        int num = answerMapper.insert(answer);
        if(num!=1){
            throw new ServiceException("服务器忙");
        }
//        Question question = questionMapper.selectById(answerVo.getQuestionId());
//        question.setStatus(1);
//        num = questionMapper.updateById(question);
        num = questionMapper.updateStatus(1, answerVo.getQuestionId());
        if(num!=1){
            throw new ServiceException("服务器忙");
        }
        return answer;
    }

    @Override
    public List<Answer> getAnswersByQuestionId(Long questionId) {
//        QueryWrapper<Answer> query = new QueryWrapper<>();
//        query.eq("quest_id",questionId);
//        List<Answer> answers = answerMapper.selectList(query);
        List<Answer> answers = answerMapper.findAnswersWithCommentsByQuestionId(questionId);
        Question question = questionMapper.selectById(questionId);
        question.setPageViews(question.getPageViews()+1);
        int num = questionMapper.updateById(question);
        if(num!=1){
            throw new ServiceException("服务器忙");
        }
        return answers;
    }

    @Transactional
    @Override
    public int accept(Long answerId, String username) {
        User user = userMapper.findUserByUsername(username);
        Answer answer = answerMapper.selectById(answerId);
        Question question = questionMapper.selectById(answer.getQuestId());
        if(answer==null || answer.getAcceptStatus()==1){
            return 0;   //失败
        }
        if (user.getId() != question.getUserId()){
            return 2;   //失败
        }
        int num = answerMapper.updateAcceptStatus(1, answerId);
        if(num!=1){
            throw new ServiceException("服务器忙");
        }
        //修改question表的状态
        num=questionMapper.updateStatus(Question.SOLVED,answer.getQuestId());
        if(num!=1){
            throw new ServiceException("服务器忙");
        }
        return 1;   //成功
    }
}
