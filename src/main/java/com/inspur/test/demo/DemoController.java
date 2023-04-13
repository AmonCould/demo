package com.inspur.test.demo;

import com.inspur.test.demo.websocket.WebSocketServer;
import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import util.SelfResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
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

    @ResponseBody
    @GetMapping("/page")
    public ModelAndView page() {
        return new ModelAndView("webSocket");
    }
    /*
     * description: WebSocket请求
     * author: jiangyf
     * date: 2023/4/13 19:22
     * @param message
     * @param toUID
     * @return org.springframework.http.ResponseEntity<java.lang.String>
     */
    @RequestMapping("/push/{toUID}")
    public ResponseEntity<String> pushToClient(String message, @PathVariable String toUID) throws IOException {
        WebSocketServer.sendInfo(message, toUID);
        return ResponseEntity.ok("Send Success!");
    }



}
