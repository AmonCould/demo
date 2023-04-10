package com.inspur.test.demo.factory;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class verifyContext {
    private final Map<String, verifyInterface> payMap = new ConcurrentHashMap<>();

//    @Autowired
//    private ApplicationContext applicationContext;
}
