package space.kuikuioj.kuikuiojbankendcompetition.controller.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import space.kuikuioj.kuikuiojbankendcommon.utils.JwtLoginUtils;
import space.kuikuioj.kuikuiojbankendcompetition.mapper.CompetitionParticipantMapper;
import space.kuikuioj.kuikuiojbankendcompetition.service.CompetitionQuestionService;
import space.kuikuioj.kuikuiojbankendcompetition.service.CompetitionService;
import space.kuikuioj.kuikuiojbankendmodel.entity.CompetitionParticipant;


import java.util.List;

/**
 * 竞赛管理控制器
 * @author kuikui
 * @date 2025/4/28 16:40
 */
@RestController
@RequestMapping("/inner")
@Slf4j
public class CompetitionControllerInner {

    @Resource
    private CompetitionParticipantMapper competitionParticipantMapper;

    @PostMapping("/selectOne")
    public CompetitionParticipant selectOne(@RequestParam("competitionId") Long competition_id, @RequestParam("userId") Long user_id){
        QueryWrapper<CompetitionParticipant> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("competition_id", competition_id)
                .eq("user_id", user_id);
        CompetitionParticipant participant = competitionParticipantMapper.selectOne(queryWrapper);
        return participant;
    }

} 