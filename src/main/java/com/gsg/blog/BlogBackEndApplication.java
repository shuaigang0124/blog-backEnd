package com.gsg.blog;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
@ComponentScan("com.gsg")
@MapperScan("com.gsg.blog.mapper")
@Slf4j
public class BlogBackEndApplication {
    public static void main(String[] args) {
        ConfigurableEnvironment environment = SpringApplication.run(BlogBackEndApplication.class, args).getEnvironment();
        log.info("\n----------------------------------------------------------\n\t"+
                "启动成功！！\n\t"+
                "服务名:\t{} \n\t" +
                "地址: \t[http://localhost:{}]\n\t"
                + "----------------------------------------------------------",
                environment.getProperty("spring.application.name"),
                environment.getProperty("server.port"));
    }

}
