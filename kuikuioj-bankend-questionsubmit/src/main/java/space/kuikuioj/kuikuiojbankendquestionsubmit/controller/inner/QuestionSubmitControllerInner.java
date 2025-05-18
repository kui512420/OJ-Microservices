package space.kuikuioj.kuikuiojbankendquestionsubmit.controller.inner;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import space.kuikuioj.kuikuiojbankendcommon.back.BaseResponse;
import space.kuikuioj.kuikuiojbankendcommon.back.ResultUtils;
import space.kuikuioj.kuikuiojbankendcommon.utils.JwtLoginUtils;
import space.kuikuioj.kuikuiojbankendmodel.dto.SubmitListRequest;
import space.kuikuioj.kuikuiojbankendmodel.dto.SubmitRankRequest;
import space.kuikuioj.kuikuiojbankendmodel.dto.SubmitRequest;
import space.kuikuioj.kuikuiojbankendmodel.dto.UserCommitRequest;
import space.kuikuioj.kuikuiojbankendmodel.entity.Question;
import space.kuikuioj.kuikuiojbankendmodel.entity.QuestionSubmit;
import space.kuikuioj.kuikuiojbankendquestionsubmit.mapper.QuestionSubmitMapper;
import space.kuikuioj.kuikuiojbankendquestionsubmit.service.QuestionSubmitService;

import java.io.File;
import java.util.List;

/**
 * @author kuikui
 * @date 2025/4/10 23:31
 */
@RestController
@RequestMapping("/inner")
public class QuestionSubmitControllerInner {

    @Resource
    private QuestionSubmitService questionSubmitService;
    @Resource
    private QuestionSubmitMapper questionSubmitMapper;

    @PostMapping("/sub")
    public Long submitQuestion(@RequestBody SubmitRequest submitRequest) {
        // 保存 用户提交的信息
        Long subId = questionSubmitService.submit(submitRequest);
        return subId;
    }

    @GetMapping("/selectCount")
    public Long selectCount(@RequestParam Long userId , @RequestParam Long competitionId) {
        QueryWrapper<QuestionSubmit> submitQueryWrapper = new QueryWrapper<>();
        submitQueryWrapper.eq("userId", userId);
        submitQueryWrapper.eq("competitionId", competitionId);
        Long submitCount = questionSubmitMapper.selectCount(submitQueryWrapper);
        return submitCount;
    }
    @GetMapping("/selectAcceptCount")
    public Long selectAcceptCount(@RequestParam Long userId , @RequestParam Long competitionId) {
        QueryWrapper<QuestionSubmit> submitQueryWrapper = new QueryWrapper<>();
        submitQueryWrapper.eq("userId", userId);
        submitQueryWrapper.eq("competitionId", competitionId);
        submitQueryWrapper.eq("status", 2); // 2表示通过
        Long acceptCount = questionSubmitMapper.selectCount(submitQueryWrapper);
        return acceptCount;
    }
    @GetMapping("/selectList")
    public List<QuestionSubmit> selectList(@RequestParam Long userId , @RequestParam Long competitionId) {
        QueryWrapper<QuestionSubmit> submitQueryWrapper = new QueryWrapper<>();
        submitQueryWrapper.eq("userId", userId);
        submitQueryWrapper.eq("competitionId", competitionId);
        submitQueryWrapper.eq("status", 2); // 2表示通过
        List<QuestionSubmit> questionSubmits = questionSubmitMapper.selectList(submitQueryWrapper);
        return questionSubmits;
    }
    @GetMapping("/selectList2")
    public List<QuestionSubmit> selectList2(@RequestParam Long userId , @RequestParam Long competitionId, @RequestParam Long questionId) {
        QueryWrapper<QuestionSubmit> submitQueryWrapper = new QueryWrapper<>();
        submitQueryWrapper.eq("userId", userId);
        submitQueryWrapper.eq("competitionId", competitionId);
        submitQueryWrapper.eq("questionId", questionId);
        submitQueryWrapper.orderByDesc("createTime");  // 按提交时间降序排序
        List<QuestionSubmit> questionSubmits = questionSubmitMapper.selectList(submitQueryWrapper);
        return questionSubmits;
    }
    @GetMapping("/updateById")
    public void updateById(@RequestParam Long id,@RequestParam Long competitionId) {
        // 更新提交记录，添加竞赛ID
        UpdateWrapper<QuestionSubmit> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id );
        QuestionSubmit updateSubmit = new QuestionSubmit();
        updateSubmit.setCompetitionId(competitionId);
        questionSubmitMapper.update(updateSubmit, updateWrapper);
    }
    @GetMapping("/getCount")
    public Long getCount() {
        // 查询未删除的用户数量
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isDelete", 0);
        return questionSubmitMapper.selectCount(queryWrapper);
    }
}
