package com.example.todoapi.member;

import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member("testUser");
    }

    @Test
    void save() {
        memberRepository.save(testMember);
        memberRepository.flushAndClear();

        Member foundMember = memberRepository.findById(testMember.getId());
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getUsername()).isEqualTo("testUser");
    }

    @Test
    void findById() {
        memberRepository.save(testMember);
        memberRepository.flushAndClear();

        Member foundMember = memberRepository.findById(testMember.getId());

        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getUsername()).isEqualTo(testMember.getUsername());
    }

    @Test
    void findByUsername() {
        memberRepository.save(testMember);
        memberRepository.flushAndClear();

        Member foundMember = memberRepository.findByUsername("testUser");

        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getId()).isEqualTo(testMember.getId());
    }

    @Test
    void findByUsername_NotFound() {
        memberRepository.save(testMember);
        memberRepository.flushAndClear();

        assertThatThrownBy(() -> memberRepository.findByUsername("nonexistent"))
                .isInstanceOf(NoResultException.class);
    }

    @Test
    void findAll() {
        memberRepository.save(testMember);
        Member secondMember = new Member("testUser2");
        memberRepository.save(secondMember);
        memberRepository.flushAndClear();

        List<Member> members = memberRepository.findAll();

        assertThat(members).hasSize(2);
        assertThat(members).extracting("username")
                .containsExactlyInAnyOrder("testUser", "testUser2");
    }

    @Test
    void deleteById() {
        memberRepository.save(testMember);
        memberRepository.flushAndClear();

        memberRepository.deleteById(testMember.getId());
        memberRepository.flushAndClear();

        Member foundMember = memberRepository.findById(testMember.getId());
        assertThat(foundMember).isNull();
    }
}