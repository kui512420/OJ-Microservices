package space.kuikuioj.kuikuibankenduser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import space.kuikuioj.kuikuibankenduser.mapper.LoginLogMapper;
import space.kuikuioj.kuikuibankenduser.mapper.UserMapper;
import space.kuikuioj.kuikuibankenduser.service.StatisticsService;
import space.kuikuioj.kuikuiojbankendmodel.entity.Question;
import space.kuikuioj.kuikuiojbankendmodel.entity.User;
import space.kuikuioj.kuikuiojbankendserviceclient.QuestionFeignClient;
import space.kuikuioj.kuikuiojbankendserviceclient.QuestionSubmitFeignClient;
import space.kuikuioj.kuikuiojbankendcommon.back.BaseResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private UserMapper userMapper;
    
    @Resource
    private QuestionFeignClient questionFeignClient;

    @Resource
    private QuestionSubmitFeignClient questionSubmitFeignClient;
    
    @Resource
    private LoginLogMapper loginLogMapper;

    @Override
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> result = new HashMap<>();
        
        // 收集各类统计数据
        result.put("userCount", getUserCount());
        result.put("questionCount", getQuestionCount());
        result.put("submitCount", getSubmitCount());
        result.put("logCount", getLogCount());
        
        return result;
    }

    @Override
    public long getUserCount() {
        // 查询未删除的用户数量
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isDelete", 0);
        return userMapper.selectCount(queryWrapper);
    }

    @Override
    public long getQuestionCount() {
        return questionFeignClient.getCount();
    }

    @Override
    public long getSubmitCount() {
        // 查询提交数量
        return questionSubmitFeignClient.getCount();
    }

    @Override
    public long getLogCount() {
        // 查询日志数量
        return loginLogMapper.selectCount(null);
    }
} 