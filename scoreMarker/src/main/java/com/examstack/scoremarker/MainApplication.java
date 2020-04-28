package com.examstack.scoremarker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by viruser on 2019/7/8.
 */
@Configuration
@ComponentScan
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args){
    /*    new SpringApplicationBuilder(MainApplication .class)
                .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
                .run(args);*/
        SpringApplication.run(MainApplication.class, args);
    }

}
