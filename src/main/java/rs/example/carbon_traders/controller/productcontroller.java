package rs.example.carbon_traders.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class productcontroller {
    @RequestMapping("/hello")
    public String hello() {
        return "Hello, World!ddb";
    }
}
