package com.inspur.test.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.SelfResponse;

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
    public SelfResponse arrayList() {
        return demoService.arrayList();
    }

    @GetMapping("/xmlTransform")
    public SelfResponse xmlTransform(@RequestBody Map<String, Object> json) {
        return demoService.xmlTransform(json);

    }

    @GetMapping("/verifyData")
    public SelfResponse verifyData(@RequestBody Map<String, Object> json) {
        return demoService.verifyData(json);

    }

    @GetMapping("/decimalCal")
    public SelfResponse decimalCal() {
        return demoService.decimalCal();

    }

    @PostMapping("/decimalBattle")
    public SelfResponse decimalBattle(@RequestParam("input1") String input1,
                                      @RequestParam("input2") String input2,
                                      @RequestParam("runTimes") String runTimes) {
        return demoService.decimalBattle(input1, input2, runTimes);

    }
    @GetMapping("/threadUpdate")
    public SelfResponse threadUpdate() {
        return demoService.threadUpdate();

    }

    @PostMapping("/Strsplit")
    public SelfResponse decimalBattle(@RequestParam("idStr") String idStr) {
        return demoService.Strsplit(idStr);

    }

    @PostMapping("/EntitySetTest")
    public SelfResponse EntitySetTest(@RequestParam("text") String text) {
        return demoService.EntitySetTest(text);

    }

}
