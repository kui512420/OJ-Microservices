package space.kuikuioj.kuikuibankendquestion;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRabbit
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"space.kuikuioj"},exclude = {OpenAiAutoConfiguration .class})
@MapperScan("space.kuikuioj.kuikuibankendquestion.mapper")
@ComponentScan({"space.kuikuioj.kuikuibankendquestion", "space.kuikuioj.kuikuiojbankendcommon"})
public class KuikuiBankendQuestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(KuikuiBankendQuestionApplication.class, args);
    }

}
