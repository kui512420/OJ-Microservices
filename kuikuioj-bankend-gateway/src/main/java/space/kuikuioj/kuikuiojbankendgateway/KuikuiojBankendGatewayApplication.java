package space.kuikuioj.kuikuiojbankendgateway;

import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, OpenAiAutoConfiguration.class})
@ComponentScan(basePackages = {"space.kuikuioj.kuikuiojbankendgateway"})
public class KuikuiojBankendGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(KuikuiojBankendGatewayApplication.class, args);
	}

}
