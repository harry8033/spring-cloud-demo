package com.hr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Author: Ru He
 * Date: Created in 2017/12/14.
 * Description:
 */
@SpringBootApplication
//@EnableDiscoveryClient
@EnableZuulProxy
public class EurekaClientApp {

    public static void main(String[] args) {
        SpringApplication.run(EurekaClientApp.class, args);
    }

}
