package com.hr.config;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Ru He
 * Date: Created in 2018/12/14.
 * Description:
 */
@RestController
public class ConfigServerController {

    @RequestMapping("/getName")
    public String getName(){

        return "config-server";
    }

}
