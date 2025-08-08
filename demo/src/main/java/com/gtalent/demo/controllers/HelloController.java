package com.gtalent.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    //第12~15列為一個API
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    //第18~21列為一個API
    @GetMapping("/hello2")
    public int hello2 () {
        return 123;
    }

    @GetMapping("/v1/rest/datastore/F-C0032-001")
    public String forecastWeather() {
        return "台中市明天晴朗";
    }

    @PostMapping("/post")
    public String post() {
        return "post";
    }

    @PutMapping("/hello")
    public String put() {
        return "put";
    }

}
