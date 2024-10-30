package com.example.todoapi.friend;

import com.example.todoapi.member.Member;
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
class FriendRepositoryTest {

    @Autowired
    FriendRepository friendRepository;

    private Member fromMember;
    private Member toMember;
    private Friend testFriend;

    @BeforeEach
    void setUp() {
        fromMember = new Member("user1");
        toMember = new Member("user2");
        testFriend = new Friend(fromMember, toMember);
    }

    @Test
    void save() {
        // when
        friendRepository.save(testFriend);
        friendRepository.flushAndClear();

        // then
        Friend foundFriend = friendRepository.findById(testFriend.getId());
        assertThat(foundFriend).isNotNull();
        assertThat(foundFriend.getFromMember().getUsername()).isEqualTo("user1");
        assertThat(foundFriend.getToMember().getUsername()).isEqualTo("user2");
    }

    @Test
    void findById() {
        // given
        friendRepository.save(testFriend);
        friendRepository.flushAndClear();

        // when
        Friend foundFriend = friendRepository.findById(testFriend.getId());

        // then
        assertThat(foundFriend).isNotNull();
        assertThat(foundFriend.getFromMember()).isEqualTo(fromMember);
        assertThat(foundFriend.getToMember()).isEqualTo(toMember);
    }

    @Test
    void findAll() {
        // given
        friendRepository.save(testFriend);
        Member thirdMember = new Member("user3");
        Friend secondFriend = new Friend(fromMember, thirdMember);
        friendRepository.save(secondFriend);
        friendRepository.flushAndClear();

        // when
        List<Friend> friends = friendRepository.findAll();

        // then
        assertThat(friends).hasSize(2);
    }

    @Test
    void findAllByFromMember() {
        // given
        friendRepository.save(testFriend);
        Member thirdMember = new Member("user3");
        Friend secondFriend = new Friend(fromMember, thirdMember);
        friendRepository.save(secondFriend);
        friendRepository.flushAndClear();

        // when
        List<Friend> friends = friendRepository.findAllByFromMember(fromMember);

        // then
        assertThat(friends).hasSize(2);
        assertThat(friends).extracting("fromMember")
                .containsOnly(fromMember);
    }

    @Test
    void findAllByToMember() {
        // given
        friendRepository.save(testFriend);
        Member thirdMember = new Member("user3");
        Friend secondFriend = new Friend(thirdMember, toMember);
        friendRepository.save(secondFriend);
        friendRepository.flushAndClear();

        // when
        List<Friend> friends = friendRepository.findAllByToMember(toMember);

        // then
        assertThat(friends).hasSize(2);
        assertThat(friends).extracting("toMember")
                .containsOnly(toMember);
    }

    @Test
    void findByFromMemberAndToMember() {
        // given
        friendRepository.save(testFriend);
        friendRepository.flushAndClear();

        // when
        Friend foundFriend = friendRepository.findByFromMemberAndToMember(fromMember, toMember);

        // then
        assertThat(foundFriend).isNotNull();
        assertThat(foundFriend.getFromMember()).isEqualTo(fromMember);
        assertThat(foundFriend.getToMember()).isEqualTo(toMember);
    }

    @Test
    void findByFromMemberAndToMember_NotFound() {
        // when & then
        assertThatThrownBy(() ->
                friendRepository.findByFromMemberAndToMember(fromMember, toMember))
                .isInstanceOf(NoResultException.class);
    }

    @Test
    void deleteById() {
        // given
        friendRepository.save(testFriend);
        friendRepository.flushAndClear();

        // when
        friendRepository.deleteById(testFriend.getId());
        friendRepository.flushAndClear();

        // then
        Friend foundFriend = friendRepository.findById(testFriend.getId());
        assertThat(foundFriend).isNull();
    }
}