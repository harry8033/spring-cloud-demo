package com.hr.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by heru on 2017/11/27.
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableDiscoveryClient
public class ConfigClient {

    public static void main(String[] args) {
        //new SpringApplicationBuilder(ConfigClient.class).web(true).run(args);
        SpringApplication.run(ConfigClient.class, args);
    }

}
