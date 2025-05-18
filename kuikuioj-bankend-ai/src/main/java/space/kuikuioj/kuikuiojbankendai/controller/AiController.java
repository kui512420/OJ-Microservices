package space.kuikuioj.kuikuiojbankendai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import space.kuikuioj.kuikuiojbankendai.dto.QuestionConfingRequest;
import space.kuikuioj.kuikuiojbankendcommon.back.BaseResponse;
import space.kuikuioj.kuikuiojbankendcommon.back.ResultUtils;

import java.util.Map;
import java.util.List;

@Tag(name = "AI服务接口", description = "提供AI相关功能的接口")
@RestController
@RequestMapping("/")
public class AiController {
    private final ChatClient chatClient;
    private final StreamingChatClient streamingChatClient;
    
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;
    
    @Value("${spring.ai.openai.chat.default-system-message}")
    private String systemMessageContent;
    
    @Autowired
    public AiController(ChatClient chatClient, StreamingChatClient streamingChatClient) {
        this.chatClient = chatClient;
        this.streamingChatClient = streamingChatClient;
    }

    @Operation(summary = "生成题目", description = "根据提供的参数生成OJ题目")
    @PostMapping("/generate")
    public BaseResponse<String> ai(@Parameter(description = "题目配置请求") @RequestBody QuestionConfingRequest questionConfingRequest) {
        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("生成 ").append("题目描述是").append(questionConfingRequest.getQuestionType())
                .append("，题目难度是 ").append(questionConfingRequest.getQuestionDifficulty()).append("，测试用例条数：").append(questionConfingRequest.getQuestionCount());

        UserMessage userMessage = new UserMessage(promptBuilder.toString());
        SystemMessage systemMessage = new SystemMessage(systemMessageContent);
        
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        
        ChatResponse response = chatClient.call(prompt);
        System.out.println(response.getResult().getOutput().getContent());
        return ResultUtils.success("生成成功", response.getResult().getOutput().getContent());
    }
    
    /**
     * 流式聊天接口
     * @param message 用户消息
     * @return 流式聊天响应
     */
    @Operation(summary = "流式聊天", description = "提供流式AI聊天功能，返回SSE事件流")
    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> chat(@Parameter(description = "用户消息") @RequestBody String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return streamingChatClient.stream(prompt);
    }
    
    /**
     * 获取deepseek API余额
     * @return API余额信息
     */
    @Operation(summary = "获取API余额", description = "获取Deepseek API账户余额信息")
    @GetMapping("/balance")
    public BaseResponse<Map<String, Object>> getApiBalance() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.deepseek.com/user/balance",
                HttpMethod.GET,
                entity,
                Map.class
            );
            
            return ResultUtils.success("获取API余额成功", response.getBody());
        } catch (Exception e) {
            return ResultUtils.error(500, "获取API余额失败: " + e.getMessage(), null);
        }
    }
}