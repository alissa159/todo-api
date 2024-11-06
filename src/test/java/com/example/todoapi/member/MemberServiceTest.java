package com.example.todoapi.member;

import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원가입_성공() {
        // given
        String username = "testUser";

        // when
        Long savedId = memberService.join(username);

        // then
        Member findMember = memberService.getMemberById(savedId);
        assertThat(findMember.getUsername()).isEqualTo(username);
    }

    @Test
    void 중복_회원가입_실패() {
        // given
        String username = "testUser";
        memberService.join(username);

        // when & then
        assertThatThrownBy(() -> memberService.join(username))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 회원입니다");
    }

    @Test
    void 로그인_성공() {
        // given
        String username = "testUser";
        memberService.join(username);

        // when
        Member loginMember = memberService.login(username);

        // then
        assertThat(loginMember.getUsername()).isEqualTo(username);
    }

    @Test
    void 존재하지않는_사용자_로그인_실패() {
        // given
        String username = "nonexistentUser";

        // when & then
        assertThatThrownBy(() -> memberService.login(username))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다");
    }

    @Test
    void 회원조회_ID() {
        // given
        String username = "testUser";
        Long savedId = memberService.join(username);

        // when
        Member findMember = memberService.getMemberById(savedId);

        // then
        assertThat(findMember.getUsername()).isEqualTo(username);
    }

    @Test
    void 회원조회_Username() {
        // given
        String username = "testUser";
        memberService.join(username);

        // when
        Member findMember = memberService.getMemberByUsername(username);

        // then
        assertThat(findMember.getUsername()).isEqualTo(username);
    }

    @Test
    void 전체회원_조회() {
        // given
        memberService.join("user1");
        memberService.join("user2");
        memberService.join("user3");

        // when
        List<Member> allMembers = memberService.getAllMembers();

        // then
        assertThat(allMembers).hasSize(3);
    }

    @Test
    void 회원삭제() {
        // given
        Long memberId = memberService.join("testUser");

        // when
        memberService.deleteMember(memberId);

        // then
        assertThatThrownBy(() -> memberService.getMemberById(memberId))
                .isInstanceOf(NoResultException.class);
    }

    @Test
    void 유효하지않은_사용자이름_가입실패() {
        // Empty username
        assertThatThrownBy(() -> memberService.join(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비워둘 수 없습니다");

        // Too short username
        assertThatThrownBy(() -> memberService.join("ab"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("3자 이상");

        // Too long username
        assertThatThrownBy(() -> memberService.join("a".repeat(21)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("20자 이하");
    }
}