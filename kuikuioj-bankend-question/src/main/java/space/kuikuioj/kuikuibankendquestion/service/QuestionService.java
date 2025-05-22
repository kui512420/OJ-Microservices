package space.kuikuioj.kuikuibankendquestion.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import space.kuikuioj.kuikuiojbankendmodel.dto.QuestionPostRequest;
import space.kuikuioj.kuikuiojbankendmodel.dto.QuestionRequest;
import space.kuikuioj.kuikuiojbankendmodel.entity.Question;
import space.kuikuioj.kuikuiojbankendmodel.vo.QuestionListVo;
import space.kuikuioj.kuikuiojbankendmodel.vo.QuestionViewVo;
import space.kuikuioj.kuikuiojbankendmodel.vo.TagCountVO;

import java.util.List;

/**
* @author 30767
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2025-03-16 20:33:35
*/
public interface QuestionService extends IService<Question> {
    Page<QuestionListVo> selectAllQuestion(QuestionRequest questionRequest);
    QuestionViewVo findOne(Long id);
    Integer put(QuestionPostRequest questionPostRequest);

    void submit();

    Question findDetail(Long id);

    Integer updateInfo(QuestionPostRequest questionPostRequest);

    List<Question> queryQuestions();

    /**
     * 获取数量最多的前4个题目标签及其数量
     * @return List<TagCountVO>
     */
    List<TagCountVO> getTop4PopularTags();
}
