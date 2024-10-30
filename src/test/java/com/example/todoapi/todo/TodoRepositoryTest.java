package com.example.todoapi.todo;

import com.example.todoapi.member.Member;
import com.example.todoapi.member.MemberRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //8080포트로 실제 어플리케이션 실행하는 것처럼 테스트
public class TodoRepositoryTest {
    @Autowired
    private TodoRepository todoRepository; //빈 주입받기

    @Autowired
    private MemberRepository memberRepository;


    //in memory database
    @AfterAll
    public static void doNotFinish() {
        System.out.println("test finished");
        while (true) {
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    void todoSaveTest() {
        //트랜잭셕의 시작

        Todo todo = new Todo("todo content", false, null);
        todoRepository.save(todo);

        //트랜잭션 종료-> 커밋
        //에러가 발생했을 때는 자동으로 롤백

        //테스트 환경 기준으로는, 에러가 발생하지 않아도 테스트가 끝나면 자동으로 롤백, 수동으로 롤백 꺼주면 됨

        Assertions.assertThat(todo.getId()).isNotNull();
    }

    @Test
    @Transactional
    void todoFindOneByIdTest() {
        //given
        Todo todo = new Todo("todo content", false, null);
        todoRepository.save(todo);

        todoRepository.flushAndClear();

        //when
        Todo findTodo = todoRepository.findById(todo.getId());

        //then
        Assertions.assertThat(findTodo.getId()).isEqualTo(todo.getId());
    }

    @Test
    @Transactional
    void todoFindAllTest() {
        Todo todo1 = new Todo("todo content1", false, null);
        Todo todo2 = new Todo("todo content2", false, null);
        Todo todo3 = new Todo("todo content3", false, null);
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        List<Todo> todoList = todoRepository.findAll();

        Assertions.assertThat(todoList).hasSize(3);


    }

    @Test
    @Transactional
    @Rollback(false)
    void todoUpdateTest() {
        Todo todo1 = new Todo("todo content1", false, null);
        todoRepository.save(todo1);

        todoRepository.flushAndClear();

        Todo findTodo1 = todoRepository.findById(todo1.getId());
        findTodo1.updateContent("new Content");

    }

    @Test
    @Transactional
    @Rollback(false)
    void todoDeleteTest() {
        Todo todo1 = new Todo("todo content1", false, null);
        Todo todo2 = new Todo("todo content2", false, null);
        todoRepository.save(todo1);
        todoRepository.save(todo2);

        todoRepository.flushAndClear();

        todoRepository.deleteById(todo1.getId());

    }


    @Test
    @Transactional
    void todoFindAllByMemberTest() {

        Member member1 = new Member();
        Member member2 = new Member();
        memberRepository.save(member1);
        memberRepository.save(member2);


        Todo todo1 = new Todo("todo content1", false, member1);
        Todo todo2 = new Todo("todo content2", false, member1);
        Todo todo3 = new Todo("todo content3", false, member2);
        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        List<Todo> member1TodoList = todoRepository.findAllByMember(member1);
        List<Todo> member2TodoList = todoRepository.findAllByMember(member2);

        Assertions.assertThat(member1TodoList).hasSize(2);
        Assertions.assertThat(member2TodoList).hasSize(1);


    }

}
