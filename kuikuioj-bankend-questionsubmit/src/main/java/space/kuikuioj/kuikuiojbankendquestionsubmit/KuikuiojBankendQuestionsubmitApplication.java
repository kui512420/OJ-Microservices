package space.kuikuioj.kuikuiojbankendquestionsubmit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {OpenAiAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "space.kuikuioj.kuikuiojbankendserviceclient")
@MapperScan("space.kuikuioj.kuikuiojbankendquestionsubmit.mapper")
@ComponentScan({"space.kuikuioj.kuikuiojbankendquestionsubmit","space.kuikuioj.kuikuiojbankendcommon"})
public class KuikuiojBankendQuestionsubmitApplication {

	public static void main(String[] args) {
		SpringApplication.run(KuikuiojBankendQuestionsubmitApplication.class, args);
	}

}
