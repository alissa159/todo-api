package com.example.todoapi.todo;

import com.example.todoapi.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TodoServiceTest {

    @Autowired
    TodoService todoService;

    @Autowired
    TodoRepository todoRepository;

    private Member testMember;
    private Member otherMember;

    @BeforeEach
    void setUp() {
        // 테스트용 멤버 생성
        testMember = new Member("testUser");
        otherMember = new Member("otherUser");
    }

    @Test
    void 할일생성() {
        // given
        String content = "테스트 할일";

        // when
        Long todoId = todoService.createTodo(content, testMember);

        // then
        Todo findTodo = todoService.getTodoById(todoId);
        assertThat(findTodo.getContent()).isEqualTo(content);
        assertThat(findTodo.getMember()).isEqualTo(testMember);
        assertThat(findTodo.isChecked()).isFalse();
    }

    @Test
    void 할일내용_수정() {
        // given
        Long todoId = todoService.createTodo("원래 할일", testMember);
        String newContent = "수정된 할일";

        // when
        todoService.updateTodoContent(todoId, newContent, testMember);

        // then
        Todo updatedTodo = todoService.getTodoById(todoId);
        assertThat(updatedTodo.getContent()).isEqualTo(newContent);
    }

    @Test
    void 할일체크_토글() {
        // given
        Long todoId = todoService.createTodo("테스트 할일", testMember);
        Todo todo = todoService.getTodoById(todoId);
        boolean originalStatus = todo.isChecked();

        // when
        todoService.toggleTodoCheck(todoId, testMember);

        // then
        Todo toggledTodo = todoService.getTodoById(todoId);
        assertThat(toggledTodo.isChecked()).isEqualTo(!originalStatus);
    }

    @Test
    void 멤버별_할일목록_조회() {
        // given
        todoService.createTodo("할일 1", testMember);
        todoService.createTodo("할일 2", testMember);
        todoService.createTodo("다른 멤버의 할일", otherMember);

        // when
        List<Todo> memberTodos = todoService.getTodosByMember(testMember);

        // then
        assertThat(memberTodos).hasSize(2);
        assertThat(memberTodos).allMatch(todo -> todo.getMember().equals(testMember));
    }

    @Test
    void 할일삭제() {
        // given
        Long todoId = todoService.createTodo("삭제할 할일", testMember);

        // when
        todoService.deleteTodo(todoId, testMember);

        // then
        assertThatThrownBy(() -> todoService.getTodoById(todoId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 권한없는_할일수정_실패() {
        // given
        Long todoId = todoService.createTodo("다른 사람의 할일", otherMember);

        // when & then
        assertThatThrownBy(() -> todoService.updateTodoContent(todoId, "수정된 내용", testMember))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("권한이 없습니다");
    }

    @Test
    void 잘못된_할일내용_생성실패() {
        // given
        String emptyContent = "";

        // when & then
        assertThatThrownBy(() -> todoService.createTodo(emptyContent, testMember))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비워둘 수 없습니다");
    }
}