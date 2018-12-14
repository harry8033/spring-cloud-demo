package com.hr.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Created by heru on 2017/11/27.
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServier {

    public static void main(String[] args){
        //SpringApplication.run(ConfigServier.class, args);
        new SpringApplicationBuilder(ConfigServier.class).web(true).run(args);
    }

}
