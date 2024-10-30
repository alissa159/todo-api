package com.example.todoapi.bean;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanTest {

    ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);

    @Test
    public void getAllBeanTest() {
        //스프링 컨테이너를 설정 파일 정보를 이용해서 생성하고, 스프링 컨테이너 안에 있는 모든 빈을 조회하는 테스트
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        //context 안에 myBean이 들어있는지 검증
        Assertions.assertThat(context.getBeanDefinitionNames()).contains("myBean");

    }

    @Test
    public void getOneBeanTest() {
        MyBean myBean1 = context.getBean(MyBean.class);
        MyBean myBean2 = context.getBean(MyBean.class);

        System.out.println(myBean1);
        System.out.println(myBean2);

        Assertions.assertThat(myBean1).isSameAs(myBean2);
    }

    @Test
    public void dependencyInjection() {
        MyBean myBean = context.getBean(MyBean.class);
        MySubBean mySubBean = context.getBean(MySubBean.class);

        System.out.println(myBean.getMySubBean()); //myBean에서 가져온 MySubBean
        System.out.println(mySubBean); //spring 컨테이너에서 가져온 MySubBean

        Assertions.assertThat(myBean.getMySubBean()).isSameAs(mySubBean);
    }

}
