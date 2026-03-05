package com.feijimiao.xianyuassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class XianYuAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(XianYuAssistantApplication.class, args);
        System.out.println("\n----------------------------------------------------------");
        System.out.println("  应用启动成功！访问地址：");
        System.out.println("  本地:    http://localhost:12400");
        System.out.println("  API文档: http://localhost:12400/doc.html");
        System.out.println("----------------------------------------------------------\n");
    }

}
