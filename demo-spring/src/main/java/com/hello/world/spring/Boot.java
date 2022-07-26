package com.hello.world.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Boot {
    public static void main(String[] args) {
        SpringApplication run = new SpringApplication(Boot.class);
        run.run(args);
    }

    @RequestMapping(value = {"", "/", "index", "index.html"})
    public String index() {
        return "<h2>It working !</h2>";
    }
}
