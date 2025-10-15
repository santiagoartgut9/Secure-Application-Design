package com.arep.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public Map<String,String> hello(Principal principal) {
        String user = (principal != null) ? principal.getName() : "anonymous";
        return Map.of("message", "Hello " + user);
    }
}
