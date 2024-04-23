package cn.itsource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(RuleApplication.class, args);
    }
}
