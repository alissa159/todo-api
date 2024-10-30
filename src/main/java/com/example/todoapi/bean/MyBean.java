package com.example.todoapi.bean;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class MyBean {

    private final MySubBean mySubBean;

}
