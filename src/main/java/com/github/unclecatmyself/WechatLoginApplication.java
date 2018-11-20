package com.github.unclecatmyself;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.myself.winter"})
@ComponentScan({"com.github.unclecatmyself"})
public class WechatLoginApplication {

	public static void main(String[] args) {
		SpringApplication.run(WechatLoginApplication.class, args);
	}
}
