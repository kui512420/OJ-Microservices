package space.kuikuioj.kuikuibankenduser.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 3配置类
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KuiKui OJ 用户服务 API文档")
                        .description("这是KuiKui OJ用户服务的API文档，提供了用户管理相关功能的接口说明")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("KuiKui")
                                .email("kuikui@example.com")
                                .url("https://kuikuioj.space"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("KuiKui OJ项目文档")
                        .url("https://kuikuioj.space/docs"));
    }
} 