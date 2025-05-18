package space.kuikuioj.kuikuiojbankendcompetition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import space.kuikuioj.kuikuiojbankendmodel.entity.CompetitionQuestion;

/**
 * 竞赛题目关联Mapper
 * @author kuikui
 * @date 2025/4/28 15:55
 */
@Mapper
public interface CompetitionQuestionMapper extends BaseMapper<CompetitionQuestion> {
} 