package com.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@Component
public class WebStaticFileConfig extends WebMvcConfigurerAdapter {      //配置本机静态资源的访问地址

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/public/" );
        registry.addResourceHandler("uploadFiles" + "/**").addResourceLocations("file:" + "C:/uploadFiles/" );
        super.addResourceHandlers(registry);

    }

}
