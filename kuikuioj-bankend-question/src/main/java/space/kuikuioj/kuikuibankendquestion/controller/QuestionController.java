package space.kuikuioj.kuikuibankendquestion.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
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
import space.kuikuioj.kuikuiojbankendmodel.vo.QuestionListVo;
import space.kuikuioj.kuikuiojbankendmodel.vo.QuestionViewVo;
import space.kuikuioj.kuikuiojbankendmodel.vo.TagCountVO;

import java.io.IOException;
import java.util.List;

/**
 * @author kuikui
 * @date 2025/3/16 19:10
 */
@RestController
@RequestMapping("/")
public class QuestionController {

    @Resource
    private QuestionService questionService;
    @Resource
    private ExportUtil exportUtil;
    @Resource
    private JwtLoginUtils jwtLoginUtils;
    /**
     * 导出文件
     * @param response
     * @throws IOException
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        try{
            List<Question> users1 = questionService.queryQuestions();
            exportUtil.export(response,"xxx.xls",users1,Question.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping("/questions")
    public BaseResponse<Page<QuestionListVo>> questions(@RequestBody QuestionRequest questionRequest) {
        Page<QuestionListVo> pageInfo = questionService.selectAllQuestion(questionRequest);
        return ResultUtils.success("查询列表成功",pageInfo);
    }
    @GetMapping("/questionInfo/{id}")
    public BaseResponse<QuestionViewVo> questionInfo(@PathVariable("id") Long id) {
        QuestionViewVo pageInfo = questionService.findOne(id);
        return ResultUtils.success("获取题目信息成功",pageInfo);
    }
    @GetMapping("/questionInfoDetail/{id}")
    public BaseResponse<Question> questionInfoDetail(@PathVariable("id") Long id) {
        Question question = questionService.findDetail(id);
        return ResultUtils.success("获取题目信息成功",question);
    }

    @PostMapping("/question")
    public BaseResponse<Integer> question(@RequestBody QuestionPostRequest questionPostRequest, @RequestHeader(value = "Accesstoken",required = false) String accessToken) {
        Long id = null;

        id = Long.valueOf((String) jwtLoginUtils.jwtPeAccess(accessToken).get("id")) ;
        questionPostRequest.setUserId(id);
        Integer count = questionService.put(questionPostRequest);
        if(count > 0){
            return ResultUtils.success("新增题目成功",count);
        }else{
            return ResultUtils.error(50000,"新增题目失败",count);
        }
    }

    @PostMapping("/submit")
    public BaseResponse<String> submit(){
        questionService.submit();
        return ResultUtils.success("","");
    }
    /**
     * 删除单个题目
     * @param id
     * @return
     */
    @DeleteMapping("/question/{id}")
    public BaseResponse<Boolean> deleteQuestion(@PathVariable Long id) {
        boolean result = questionService.removeById(id);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目不存在");
        }
        return ResultUtils.success("删除成功",true);
    }
    /**
     * 删除多个题目
     * @param ids 题目 ID 列表
     * @return 删除结果
     */
    @DeleteMapping("/question")
    public BaseResponse<Boolean> deleteQuestions(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(ErrorCode.PARMS_ERROR, "题目 ID 列表不能为空");
        }
        boolean result = questionService.removeByIds(ids);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "部分或全部题目删除失败");
        }
        return ResultUtils.success("删除成功", true);
    }

    @PutMapping("question")
        public BaseResponse<Integer> updateQuestion(@RequestHeader(value = "AccessToken",required = false) String accessToken,@RequestBody  QuestionPostRequest questionPostRequest) {
        Long id = null;
        try{
            JwtLoginUtils jwtLoginUtils = new JwtLoginUtils();
            id = (Long) jwtLoginUtils.jwtPeAccess(accessToken).get("id");
            questionPostRequest.setUserId(id);
        }catch (Exception e){
            throw new BusinessException(ErrorCode.PARMS_ERROR,"令牌无效");
        }
        questionPostRequest.setUserId(id);
        Integer count = questionService.updateInfo(questionPostRequest);
        return ResultUtils.success("更新题目成功",count);
    }

    /**
     * 获取数量最多的前4个题目标签及其数量
     *
     * @return BaseResponse<List<TagCountVO>>
     */
    @GetMapping("/popularTags")
    public BaseResponse<List<TagCountVO>> getTop4PopularTags() {
        List<TagCountVO> popularTags = questionService.getTop4PopularTags();
        return ResultUtils.success("获取成功", popularTags);
    }
}
