package com.github.unclecatmyself;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2 //Swagger自动生成文档
@ComponentScan({"com.myself.winter"})
@ComponentScan({"com.github.unclecatmyself"})
public class WechatLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(WechatLoginApplication.class, args);
	}
}
