package com.hr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author: Ru He
 * Date: Created in 2018/12/13.
 * Description:
 */
@Controller
public class TestController {

    @ResponseBody
    @RequestMapping("/")
    public String test(){
        return "success";
    }

}
