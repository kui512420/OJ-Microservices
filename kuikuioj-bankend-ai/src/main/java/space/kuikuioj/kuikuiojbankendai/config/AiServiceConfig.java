package space.kuikuioj.kuikuiojbankendai.config;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AI服务配置类
 */
@Configuration
public class AiServiceConfig {
    
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;
    
    @Value("${spring.ai.openai.base-url}")
    private String baseUrl;
    
    /**
     * 创建StreamingChatClient Bean用于流式聊天
     * 如果Spring Boot自动配置没有提供StreamingChatClient，则我们手动创建
     */
    @Bean
    public StreamingChatClient streamingChatClient() {
        // 在Spring AI 0.8.0中，OpenAiApi使用构造函数的方式设置API密钥和基础URL
        OpenAiApi openAiApi = new OpenAiApi(baseUrl, apiKey);
        
        OpenAiChatOptions options = OpenAiChatOptions.builder()
            .withModel("deepseek-chat")
            .withTemperature(0.7f)
            .build();
            
        return new OpenAiChatClient(openAiApi)
            .withDefaultOptions(options);
    }
} 