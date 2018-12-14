package com.hr.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by heru on 2017/11/27.
 */
@RestController
@RefreshScope
public class ClientController {

    @Value("${sys.error.msg}")
    private String errorMsg;

    @ResponseBody
    @RequestMapping("/client/test")
    public String getProperty(){
        return errorMsg;
    }

}
