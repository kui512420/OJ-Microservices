package space.kuikuioj.kuikuiojbankendcompetition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import space.kuikuioj.kuikuiojbankendcompetition.mapper.CompetitionMapper;
import space.kuikuioj.kuikuiojbankendcompetition.mapper.CompetitionQuestionMapper;
import space.kuikuioj.kuikuiojbankendcompetition.service.CompetitionQuestionService;
import space.kuikuioj.kuikuiojbankendmodel.entity.CompetitionQuestion;
import space.kuikuioj.kuikuiojbankendmodel.entity.Question;
import space.kuikuioj.kuikuiojbankendmodel.vo.QuestionListVo;
import space.kuikuioj.kuikuiojbankendserviceclient.QuestionFeignClient;


import java.util.ArrayList;
import java.util.List;

@Service
public  class CompetitionQuestionServiceImpl implements CompetitionQuestionService {


    @Resource
    private CompetitionQuestionMapper competitionQuestionMapper;

    @Resource
    private QuestionFeignClient questionFeignClient;

    public List<QuestionListVo> getQuestions(Long id){
        QueryWrapper<CompetitionQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("competition_id", id);
        List<QuestionListVo> questionListVos = new ArrayList<>();
        List<CompetitionQuestion> competitionQuestions = competitionQuestionMapper.selectList(queryWrapper);
        for (CompetitionQuestion competitionQuestion : competitionQuestions) {
            Question question = questionFeignClient.selectByIdInner(competitionQuestion.getQuestionId());
            questionListVos.add(new QuestionListVo(question));
        }
        return questionListVos;
    }
}
