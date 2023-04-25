package com.weng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class ReggieTakeOutApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(ReggieTakeOutApplication.class, args);
    }

}
