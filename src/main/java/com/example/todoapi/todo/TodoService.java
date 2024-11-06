package com.example.todoapi.todo;

import com.example.todoapi.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // 할 일 생성
    @Transactional
    public Long createTodo(String content, Member member) {
        validateContent(content);
        Todo todo = new Todo(content, false, member);
        todoRepository.save(todo);
        return todo.getId();
    }

    // 할 일 단건 조회
    public Todo getTodoById(Long todoId) {
        Todo todo = todoRepository.findById(todoId);
        if (todo == null) {
            throw new NoSuchElementException("해당 할 일을 찾을 수 없습니다. ID: " + todoId);
        }
        return todo;
    }

    // 모든 할 일 조회
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // 특정 멤버의 할 일 목록 조회
    public List<Todo> getTodosByMember(Member member) {
        return todoRepository.findAllByMember(member);
    }

    // 할 일 내용 수정
    @Transactional
    public void updateTodoContent(Long todoId, String newContent, Member member) {
        validateContent(newContent);
        Todo todo = getTodoById(todoId);
        validateTodoOwner(todo, member);
        todo.updateContent(newContent);
    }

    // 할 일 체크/체크해제
    @Transactional
    public void toggleTodoCheck(Long todoId, Member member) {
        Todo todo = getTodoById(todoId);
        validateTodoOwner(todo, member);
        todo.setChecked(!todo.isChecked());
    }

    // 할 일 삭제
    @Transactional
    public void deleteTodo(Long todoId, Member member) {
        Todo todo = getTodoById(todoId);
        validateTodoOwner(todo, member);
        todoRepository.deleteById(todoId);
    }

    // 할 일 내용 유효성 검사
    private void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("할 일 내용은 비워둘 수 없습니다.");
        }
        if (content.length() > 200) {
            throw new IllegalArgumentException("할 일 내용은 200자를 초과할 수 없습니다.");
        }
    }

    // 할 일 소유자 검증
    private void validateTodoOwner(Todo todo, Member member) {
        if (!todo.getMember().getId().equals(member.getId())) {
            throw new IllegalStateException("해당 할 일에 대한 권한이 없습니다.");
        }
    }
}