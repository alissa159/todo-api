package com.example.todoapi.bean;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BeanTest2 {
    @Autowired
    private MyBean myBean;

    @Autowired
    private MySubBean mySubBean;

    @Test
    public void dependencyInjection() {
        System.out.println(myBean.getMySubBean()); //myBean에서 가져온 MySubBean
        System.out.println(mySubBean); //spring 컨테이너에서 가져온 MySubBean

        Assertions.assertThat(myBean.getMySubBean()).isSameAs(mySubBean);
    }
}
