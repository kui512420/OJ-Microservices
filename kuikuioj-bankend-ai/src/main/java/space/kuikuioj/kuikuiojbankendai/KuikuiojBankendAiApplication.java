package space.kuikuioj.kuikuiojbankendai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import space.kuikuioj.kuikuiojbankendcommon.config.AIConfig;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("space.kuikuioj.kuikuiojbankendai.mapper")
@ComponentScan(
    basePackages = {"space.kuikuioj.kuikuiojbankendai", "space.kuikuioj.kuikuiojbankendcommon"},
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AIConfig.class)
    }
)
public class KuikuiojBankendAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(KuikuiojBankendAiApplication.class, args);
    }

}
