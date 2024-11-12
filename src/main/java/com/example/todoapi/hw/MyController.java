package com.example.todoapi.hw;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyController {

    private final MyService myService;

    public void controllerMethod() {
        System.out.println("controller");
        myService.serviceMethod();
    }
}