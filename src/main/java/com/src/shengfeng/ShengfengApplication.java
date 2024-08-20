package com.src.shengfeng;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class ShengfengApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShengfengApplication.class, args);
    }
    @Bean
    public PropertiesFactoryBean systemMessage() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("messages.properties"));
        return propertiesFactoryBean;
    }
}
