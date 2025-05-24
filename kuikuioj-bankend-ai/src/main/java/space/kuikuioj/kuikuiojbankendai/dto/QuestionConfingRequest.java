package space.kuikuioj.kuikuiojbankendai.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 请求生成题目的参数配置
 */
@Schema(description = "题目生成请求参数")
public class QuestionConfingRequest {
    /**
     * 题目类型
     */
    @Schema(description = "题目类型", example = "数组排序")
    private String questionType;

    /**
     * 题目难度
     */
    @Schema(description = "题目难度", example = "中等")
    private String questionDifficulty;

    /**
     * 测试用例数量
     */
    @Schema(description = "测试用例数量", example = "5")
    private Integer questionCount;
    
    public String getQuestionType() {
        return questionType;
    }
    
    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }
    
    public String getQuestionDifficulty() {
        return questionDifficulty;
    }
    
    public void setQuestionDifficulty(String questionDifficulty) {
        this.questionDifficulty = questionDifficulty;
    }
    
    public Integer getQuestionCount() {
        return questionCount;
    }
    
    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }
} 