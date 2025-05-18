package space.kuikuioj.kuikuiojbankendquestionsubmit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.kuikuioj.kuikuiojbankendcommon.back.ErrorCode;
import space.kuikuioj.kuikuiojbankendcommon.exception.BusinessException;
import space.kuikuioj.kuikuiojbankendcommon.rabbitmq.RabbitMQProducer;
import space.kuikuioj.kuikuiojbankendmodel.dto.*;
import space.kuikuioj.kuikuiojbankendmodel.entity.CompetitionParticipant;
import space.kuikuioj.kuikuiojbankendmodel.entity.Question;
import space.kuikuioj.kuikuiojbankendmodel.entity.QuestionSubmit;
import space.kuikuioj.kuikuiojbankendquestionsubmit.mapper.QuestionSubmitMapper;
import space.kuikuioj.kuikuiojbankendquestionsubmit.service.QuestionSubmitService;
import space.kuikuioj.kuikuiojbankendserviceclient.CompetitionFeignClient;
import space.kuikuioj.kuikuiojbankendserviceclient.QuestionFeignClient;
import space.kuikuioj.kuikuiojbankendserviceclient.UserFeignClient;


/**
 * @author kuikui
 * @date 2025/4/10 23:36
 */
@Service
@Slf4j
public class QuestionSubmitServiceImpl implements QuestionSubmitService {

    @Resource
    private QuestionSubmitMapper questionSubmitMapper;
    @Resource
    private QuestionFeignClient questionFeignClient;
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private RabbitMQProducer rabbitMQProducer;
    @Resource
    private CompetitionFeignClient competitionFeignClient;
    /**
     * @todo 插入提交信息
     * @param submitRequest
     * @return
     */
    @Override
    public Long submit(SubmitRequest submitRequest) {
        String language = submitRequest.getLanguage();
        String code = submitRequest.getCode();
        long questionId = submitRequest.getQuestionId();
        long userId = submitRequest.getUserId();
        Long competitionId = submitRequest.getCompetitionId();
        
        // 如果是竞赛提交，检查用户是否已交卷
        if (competitionId != null) {

            CompetitionParticipant participant = competitionFeignClient.selectOne(competitionId,userId);
            
            // 如果用户已交卷，则禁止再次提交
            if (participant != null && participant.getIsSubmitted() != null && participant.getIsSubmitted() == 1) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已交卷，不能再提交代码");
            }
        }
        
        Question question = questionFeignClient.selectByIdInner(questionId);
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setCode(submitRequest.getCode());
        executeCodeRequest.setLanguage(submitRequest.getLanguage());
        executeCodeRequest.setQuestion(question);
        executeCodeRequest.setLanguage(language);
        executeCodeRequest.setUserId(submitRequest.getUserId());


        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setLanguage(language);
        questionSubmit.setUserId(userId);
        questionSubmit.setCode(code);
        questionSubmit.setQuestionId(questionId);
        
        // 如果有竞赛ID，则设置
        if (competitionId != null) {
            questionSubmit.setCompetitionId(competitionId);
        }

        // 查询用户是否是首次提交
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("questionId", questionId)
                .eq("userId", userId);
        boolean exists = questionSubmitMapper.selectCount(queryWrapper) == 0;
        if(exists){
            questionFeignClient.updateSubmitCountInner(questionId);
        }

        userFeignClient.updateSubmitCountInner(userId);
        questionSubmitMapper.insert(questionSubmit);
        Long questionSubmitId  = questionSubmit.getId();
        // 添加 判题任务 到队列
        executeCodeRequest.setQuestionSubmitId(questionSubmitId);
        try {
            rabbitMQProducer.sendMessage("code_exchange","routingkey", executeCodeRequest);
        }catch (Exception e){
            e.printStackTrace();
        }

        return questionSubmitId;
    }

    /**
     * @todo 查询提交信息
     * @param submitListRequest
     * @return
     */
    @Override
    public Page<QuestionSubmit> list(SubmitListRequest submitListRequest) {
        long subId = submitListRequest.getId();
        long userId = submitListRequest.getUserId();
        Integer type = submitListRequest.getType();
        Page<QuestionSubmit> page = new Page<>(submitListRequest.getPage(), submitListRequest.getSize());

        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();

        switch (type) {
            case 1: // 通过提交ID查询
                queryWrapper.eq("id", subId);
                break;
            case 2: // 通过用户ID查询
                queryWrapper.eq("userId", userId);
                break;
            case 0: // 默认查询所有
                break;
            case 3: // 查询用户某他题的提交记录
                queryWrapper.eq("questionId",submitListRequest.getQuestionId() );
                queryWrapper.eq("userId", userId);
            default:
                // 不添加特定条件，查询所有
                break;
        }

        // 添加排序条件，按提交时间降序排列
        queryWrapper.orderByDesc("createTime");

        return questionSubmitMapper.selectPage(page, queryWrapper);
    }

    @Override
    public UserCommitRequest userCommitInfo(Long userId) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("status", 2);

        QueryWrapper<QuestionSubmit> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("userId", userId);
        return UserCommitRequest.builder()
                .commitPassCount(questionSubmitMapper.selectCount(queryWrapper)+"")
                .commitCount(questionSubmitMapper.selectCount(queryWrapper2)+"")
                .build();

    }

    @Override
    public Page<QuestionSubmit> rank(SubmitRankRequest submitRankRequest) {
        Integer type = submitRankRequest.getType();
        Page<QuestionSubmit> page = new Page<>(submitRankRequest.getPage(), submitRankRequest.getCount());
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        switch (type) {
            case 1: //
                queryWrapper.orderByDesc("createTime");
                break;
            case 2: //
                queryWrapper.orderByDesc("createTime");
                break;
            default:
                // 不添加特定条件，查询所有
                break;
        }
        return null;
    }
    
    /**
     * 获取用户的所有提交记录（分页）
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页后的用户提交记录列表
     */
    @Override
    public Page<QuestionSubmit> getAllUserSubmissions(Long userId, int pageNum, int pageSize) {
        if (userId == null) {
            return new Page<>();
        }
        
        // 创建分页对象
        Page<QuestionSubmit> page = new Page<>(pageNum, pageSize);
        
        // 创建查询条件
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        // 按提交时间降序排序
        queryWrapper.orderByDesc("createTime");
        
        // 查询并返回分页结果
        return questionSubmitMapper.selectPage(page, queryWrapper);
    }
}
