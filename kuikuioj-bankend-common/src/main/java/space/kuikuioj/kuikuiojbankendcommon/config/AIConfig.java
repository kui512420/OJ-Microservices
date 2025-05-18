package space.kuikuioj.kuikuiojbankendcommon.config;
import org.springframework.ai.chat.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * @author kuikui
 * @date 2025/4/8 11:11
 */
@Configuration
public class AIConfig {

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
