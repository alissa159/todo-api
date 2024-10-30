package com.example.todoapi.todo;

import com.example.todoapi.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;
    @Column(name = "todo content", columnDefinition = "varchar(200)")
    private String content;
    @Column(name = "todo is_checked", columnDefinition = "tinyint(1)")

    private boolean isChecked;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public Todo(String content, boolean isChecked, Member member) {
        this.content = content;
        this.isChecked = isChecked;
        this.member = member;
    }

    public void updateContent(String newContent) {
        this.content = newContent; //할 일 내용 엡데이트해주는 메서드
    }
}
