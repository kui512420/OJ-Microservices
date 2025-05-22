package space.kuikuioj.kuikuiojbankendquestionsubmit.controller.inner;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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
import space.kuikuioj.kuikuiojbankendcommon.exception.BusinessException;
import space.kuikuioj.kuikuiojbankendcommon.back.ErrorCode;

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

    private static final String ADMIN_ROLE = "admin";

    private HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    @PostMapping("/sub")
    public Long submitQuestion(@RequestBody SubmitRequest submitRequest) {
        HttpServletRequest request = getCurrentRequest();
        String currentUserId = (String) request.getAttribute("currentUserId");
        String currentUserRole = (String) request.getAttribute("currentUserRole");

        if (!ADMIN_ROLE.equals(currentUserRole) && submitRequest.getUserId() != Long.parseLong(currentUserId)) {
            throw new BusinessException(ErrorCode.NOT_AUTH_ERROR, "无权为其他用户提交");
        }
        // 保存 用户提交的信息
        Long subId = questionSubmitService.submit(submitRequest);
        return subId;
    }

    @GetMapping("/selectCount")
    public Long selectCount(@RequestParam Long userId , @RequestParam Long competitionId) {
        HttpServletRequest request = getCurrentRequest();
        String currentUserId = (String) request.getAttribute("currentUserId");
        String currentUserRole = (String) request.getAttribute("currentUserRole");

        if (!ADMIN_ROLE.equals(currentUserRole) && !userId.toString().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.NOT_AUTH_ERROR, "无权查看他人信息");
        }

        QueryWrapper<QuestionSubmit> submitQueryWrapper = new QueryWrapper<>();
        submitQueryWrapper.eq("userId", userId);
        submitQueryWrapper.eq("competitionId", competitionId);
        Long submitCount = questionSubmitMapper.selectCount(submitQueryWrapper);
        return submitCount;
    }
    @GetMapping("/selectAcceptCount")
    public Long selectAcceptCount(@RequestParam Long userId , @RequestParam Long competitionId) {
        HttpServletRequest request = getCurrentRequest();
        String currentUserId = (String) request.getAttribute("currentUserId");
        String currentUserRole = (String) request.getAttribute("currentUserRole");

        if (!ADMIN_ROLE.equals(currentUserRole) && !userId.toString().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.NOT_AUTH_ERROR, "无权查看他人信息");
        }

        QueryWrapper<QuestionSubmit> submitQueryWrapper = new QueryWrapper<>();
        submitQueryWrapper.eq("userId", userId);
        submitQueryWrapper.eq("competitionId", competitionId);
        submitQueryWrapper.eq("status", 2); // 2表示通过
        Long acceptCount = questionSubmitMapper.selectCount(submitQueryWrapper);
        return acceptCount;
    }
    @GetMapping("/selectList")
    public List<QuestionSubmit> selectList(@RequestParam Long userId , @RequestParam Long competitionId) {
        HttpServletRequest request = getCurrentRequest();
        String currentUserId = (String) request.getAttribute("currentUserId");
        String currentUserRole = (String) request.getAttribute("currentUserRole");

        if (!ADMIN_ROLE.equals(currentUserRole) && !userId.toString().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.NOT_AUTH_ERROR, "无权查看他人信息");
        }

        QueryWrapper<QuestionSubmit> submitQueryWrapper = new QueryWrapper<>();
        submitQueryWrapper.eq("userId", userId);
        submitQueryWrapper.eq("competitionId", competitionId);
        submitQueryWrapper.eq("status", 2); // 2表示通过
        List<QuestionSubmit> questionSubmits = questionSubmitMapper.selectList(submitQueryWrapper);
        return questionSubmits;
    }
    @GetMapping("/selectList2")
    public List<QuestionSubmit> selectList2(@RequestParam Long userId , @RequestParam Long competitionId, @RequestParam Long questionId) {
        HttpServletRequest request = getCurrentRequest();
        String currentUserId = (String) request.getAttribute("currentUserId");
        String currentUserRole = (String) request.getAttribute("currentUserRole");

        if (!ADMIN_ROLE.equals(currentUserRole) && !userId.toString().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.NOT_AUTH_ERROR, "无权查看他人信息");
        }

        QueryWrapper<QuestionSubmit> submitQueryWrapper = new QueryWrapper<>();
        submitQueryWrapper.eq("userId", userId);
        submitQueryWrapper.eq("competitionId", competitionId);
        submitQueryWrapper.eq("questionId", questionId);
        submitQueryWrapper.orderByDesc("createTime");  // 按提交时间降序排序
        List<QuestionSubmit> questionSubmits = questionSubmitMapper.selectList(submitQueryWrapper);
        return questionSubmits;
    }
    @PostMapping("/updateById")
    public void updateById(@RequestParam Long id,@RequestParam Long competitionId) {
        HttpServletRequest request = getCurrentRequest();
        String currentUserId = (String) request.getAttribute("currentUserId");
        String currentUserRole = (String) request.getAttribute("currentUserRole");

        if (!ADMIN_ROLE.equals(currentUserRole)) {
            QuestionSubmit submitToUpdate = questionSubmitMapper.selectById(id);
            if (submitToUpdate == null) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "提交记录不存在");
            }
            if (submitToUpdate.getUserId() == null || !submitToUpdate.getUserId().toString().equals(currentUserId)) {
                throw new BusinessException(ErrorCode.NOT_AUTH_ERROR, "无权修改他人提交记录");
            }
        }

        // 更新提交记录，添加竞赛ID
        UpdateWrapper<QuestionSubmit> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id );
        QuestionSubmit updateSubmit = new QuestionSubmit();
        updateSubmit.setCompetitionId(competitionId);
        questionSubmitMapper.update(updateSubmit, updateWrapper);
    }
    @GetMapping("/getCount")
    public Long getCount() {
        HttpServletRequest request = getCurrentRequest();
        String currentUserRole = (String) request.getAttribute("currentUserRole");

        if (!ADMIN_ROLE.equals(currentUserRole)) {
            throw new BusinessException(ErrorCode.NOT_AUTH_ERROR, "无权限访问此接口");
        }

        // 查询未删除的用户数量
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isDelete", 0);
        return questionSubmitMapper.selectCount(queryWrapper);
    }
}
