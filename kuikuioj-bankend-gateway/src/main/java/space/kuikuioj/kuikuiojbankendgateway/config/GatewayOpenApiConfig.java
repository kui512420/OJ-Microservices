package space.kuikuioj.kuikuiojbankendgateway.config;

import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

/**
 * 网关Swagger配置类
 */
@Configuration
public class GatewayOpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KuiKui OJ API文档")
                        .description("KuiKui OJ平台所有微服务API文档聚合")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("KuiKui")
                                .email("kuikui@example.com")
                                .url("https://kuikuioj.space"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
} 