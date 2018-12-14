package com.hr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Author: Ru He
 * Date: Created in 2018/12/13.
 * Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class TestApp {

    public static void main(String[] args) {
        SpringApplication.run(TestApp.class);
    }
}
