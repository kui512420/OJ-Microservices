package space.kuikuioj.kuikuiojbankendcompetition.service;


import space.kuikuioj.kuikuiojbankendmodel.vo.QuestionListVo;

import java.util.List;

public interface CompetitionQuestionService {
     public List<QuestionListVo> getQuestions(Long id);
}
