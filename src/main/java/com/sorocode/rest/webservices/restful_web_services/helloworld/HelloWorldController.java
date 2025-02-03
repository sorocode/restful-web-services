package com.sorocode.rest.webservices.restful_web_services.helloworld;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// Rest API
@RestController
public class HelloWorldController {
    // /hello-world
    // "Hello World"
    @GetMapping(value = "/hello-world")
    public String helloWorld(){
        return "Hello World";
    }
}
