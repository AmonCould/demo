package com.inspur.test.demo;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import util.SelfResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@RestController
@Slf4j
public class DemoController {
    @Autowired
    DemoService demoService = new DemoService();

    @GetMapping("/hello")
    public String hello() {
        return "HELLO TESTDEOM";
    }

    @GetMapping("/arrayList")
    public SelfResponse arrayList(){
        return demoService.arrayList();
    }
    @GetMapping("/xmlTransform")
    public SelfResponse xmlTransform(@RequestBody Map<String, Object> json){
        return demoService.xmlTransform(json);

    }
    @GetMapping("/verifyData")
    public SelfResponse verifyData(@RequestBody Map<String, Object> json){
        return demoService.verifyData(json);

    }
    @GetMapping("/decimalCal")
    public SelfResponse decimalCal(){
        return demoService.decimalCal();

    }

}
