package space.kuikuioj.kuikuibankenduser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "space.kuikuioj.kuikuiojbankendserviceclient")
@MapperScan("space.kuikuioj.kuikuibankenduser.mapper")
@ComponentScan({"space.kuikuioj.kuikuibankenduser", "space.kuikuioj.kuikuiojbankendcommon"})
public class KuikuiBankendUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(KuikuiBankendUserApplication.class, args);
	}

}
