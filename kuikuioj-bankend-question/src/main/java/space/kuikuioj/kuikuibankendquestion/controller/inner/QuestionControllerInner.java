package space.kuikuioj.kuikuibankendquestion.controller.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.kuikuioj.kuikuibankendquestion.mapper.QuestionMapper;
import space.kuikuioj.kuikuibankendquestion.service.QuestionService;
import space.kuikuioj.kuikuiojbankendcommon.back.BaseResponse;
import space.kuikuioj.kuikuiojbankendcommon.back.ErrorCode;
import space.kuikuioj.kuikuiojbankendcommon.back.ResultUtils;
import space.kuikuioj.kuikuiojbankendcommon.exception.BusinessException;
import space.kuikuioj.kuikuiojbankendcommon.utils.ExportUtil;
import space.kuikuioj.kuikuiojbankendcommon.utils.JwtLoginUtils;
import space.kuikuioj.kuikuiojbankendmodel.dto.QuestionPostRequest;
import space.kuikuioj.kuikuiojbankendmodel.dto.QuestionRequest;
import space.kuikuioj.kuikuiojbankendmodel.entity.Question;
import space.kuikuioj.kuikuiojbankendmodel.entity.User;
import space.kuikuioj.kuikuiojbankendmodel.entity.UserRank;
import space.kuikuioj.kuikuiojbankendmodel.vo.QuestionListVo;
import space.kuikuioj.kuikuiojbankendmodel.vo.QuestionViewVo;

import java.io.IOException;
import java.util.List;

/**
 * @author kuikui
 * @date 2025/3/16 19:10
 */
@RestController
@RequestMapping("/inner")
public class QuestionControllerInner {

    @Autowired
    private QuestionMapper questionMapper;

    @PostMapping("/updateSubmitNum")
    public void updateSubmitCountInner(@RequestBody Long questionId) {
        UpdateWrapper<Question> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", questionId);
        updateWrapper.setSql("submitNum = submitNum + 1");
        questionMapper.update(new Question(),updateWrapper);
    }
    @GetMapping("/selectById")
    public Question selectByIdInner(@RequestParam Long questionId) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", questionId);
        return questionMapper.selectOne(queryWrapper);
    }
    @GetMapping("/getCount")
    public Long getCount() {
        // 查询未删除的用户数量
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("isDelete", 0);
        return questionMapper.selectCount(queryWrapper);
    }
}
