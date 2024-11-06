package com.example.todoapi.friend;

import com.example.todoapi.member.Member;
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
class FriendServiceTest {

    @Autowired
    FriendService friendService;

    @Autowired
    FriendRepository friendRepository;

    private Member member1;
    private Member member2;
    private Member member3;

    @BeforeEach
    void setUp() {
        member1 = new Member("user1");
        member2 = new Member("user2");
        member3 = new Member("user3");
    }

    @Test
    void 친구요청_성공() {
        // given & when
        Long friendId = friendService.sendFriendRequest(member1, member2);

        // then
        Friend friend = friendService.getFriendById(friendId);
        assertThat(friend.getFromMember()).isEqualTo(member1);
        assertThat(friend.getToMember()).isEqualTo(member2);
    }

    @Test
    void 자기자신에게_친구요청_실패() {
        // when & then
        assertThatThrownBy(() -> friendService.sendFriendRequest(member1, member1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("자기 자신과는 친구가 될 수 없습니다");
    }

    @Test
    void 이미존재하는_친구관계_요청실패() {
        // given
        friendService.sendFriendRequest(member1, member2);

        // when & then
        assertThatThrownBy(() -> friendService.sendFriendRequest(member1, member2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 친구 관계가 존재합니다");
    }

    @Test
    void 회원의_친구목록_조회() {
        // given
        friendService.sendFriendRequest(member1, member2);
        friendService.sendFriendRequest(member3, member1);

        // when
        List<Member> friends = friendService.getMemberFriends(member1);

        // then
        assertThat(friends).hasSize(2);
        assertThat(friends).contains(member2, member3);
    }

    @Test
    void 친구관계_확인() {
        // given
        friendService.sendFriendRequest(member1, member2);

        // when & then
        assertThat(friendService.areFriends(member1, member2)).isTrue();
        assertThat(friendService.areFriends(member2, member1)).isTrue(); // 양방향 체크
        assertThat(friendService.areFriends(member1, member3)).isFalse();
    }

    @Test
    void 친구관계_삭제() {
        // given
        Long friendId = friendService.sendFriendRequest(member1, member2);

        // when
        friendService.deleteFriend(friendId, member1);

        // then
        assertThat(friendService.areFriends(member1, member2)).isFalse();
    }

    @Test
    void 권한없는_친구삭제_실패() {
        // given
        Long friendId = friendService.sendFriendRequest(member1, member2);

        // when & then
        assertThatThrownBy(() -> friendService.deleteFriend(friendId, member3))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("권한이 없습니다");
    }
}