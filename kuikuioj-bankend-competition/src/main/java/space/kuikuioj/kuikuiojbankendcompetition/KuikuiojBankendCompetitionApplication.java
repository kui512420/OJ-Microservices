package space.kuikuioj.kuikuiojbankendcompetition;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(basePackages = "space.kuikuioj.kuikuiojbankendserviceclient")
@ComponentScan(basePackages = {
		"space.kuikuioj.kuikuiojbankendcommon",
		"space.kuikuioj.kuikuiojbankendcompetition"
})
@EnableScheduling
@MapperScan("space.kuikuioj.kuikuiojbankendcompetition.mapper")
public class KuikuiojBankendCompetitionApplication {

	public static void main(String[] args) {
		SpringApplication.run(KuikuiojBankendCompetitionApplication.class, args);
	}

}
