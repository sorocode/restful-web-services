package com.sorocode.rest.webservices.restful_web_services.helloworld;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @GetMapping(value = "/hello-world-bean")
    public HelloWorldBean helloWorldBean(){
        return new HelloWorldBean("Hello World");
    }

    // Path parameters
    // /users/{id}/todos/{id} => /users/2/todos/200
    // /hello-world/path-variable/{name}
    @GetMapping(value = "/hello-world/path-variable/{name}")
    public HelloWorldBean helloWorldPathVariable(@PathVariable String name){
        return new HelloWorldBean(String.format("Hello World, %s", name ));
    }
}
