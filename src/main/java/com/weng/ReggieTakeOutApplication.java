package com.weng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@ServletComponentScan
@EnableCaching
public class ReggieTakeOutApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(ReggieTakeOutApplication.class, args);
    }

}
