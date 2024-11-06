package com.example.todoapi.friend;

import com.example.todoapi.member.Member;
import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FriendService {

    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    // 친구 요청 보내기
    @Transactional
    public Long sendFriendRequest(Member fromMember, Member toMember) {
        validateFriendRequest(fromMember, toMember);
        Friend friend = new Friend(fromMember, toMember);
        friendRepository.save(friend);
        return friend.getId();
    }

    // 친구 관계 단건 조회
    public Friend getFriendById(Long friendId) {
        Friend friend = friendRepository.findById(friendId);
        if (friend == null) {
            throw new NoResultException("해당 친구 관계를 찾을 수 없습니다. ID: " + friendId);
        }
        return friend;
    }

    // 모든 친구 관계 조회
    public List<Friend> getAllFriends() {
        return friendRepository.findAll();
    }

    // 특정 회원의 친구 목록 조회 (양방향 모두 고려)
    public List<Member> getMemberFriends(Member member) {
        List<Member> friends = new ArrayList<>();

        // fromMember로 보낸 친구 관계에서 toMember들 추가
        List<Friend> fromFriends = friendRepository.findAllByFromMember(member);
        for (Friend friend : fromFriends) {
            friends.add(friend.getToMember());
        }

        // toMember로 받은 친구 관계에서 fromMember들 추가
        List<Friend> toFriends = friendRepository.findAllByToMember(member);
        for (Friend friend : toFriends) {
            friends.add(friend.getFromMember());
        }

        return friends;
    }

    // 특정 두 회원의 친구 관계 확인
    public boolean areFriends(Member member1, Member member2) {
        try {
            // 양방향 모두 확인
            Friend friend1 = friendRepository.findByFromMemberAndToMember(member1, member2);
            return true;
        } catch (NoResultException e1) {
            try {
                Friend friend2 = friendRepository.findByFromMemberAndToMember(member2, member1);
                return true;
            } catch (NoResultException e2) {
                return false;
            }
        }
    }

    // 친구 관계 삭제 (친구 삭제)
    @Transactional
    public void deleteFriend(Long friendId, Member requestingMember) {
        Friend friend = getFriendById(friendId);
        validateFriendDeletion(friend, requestingMember);
        friendRepository.deleteById(friendId);
    }

    // 친구 요청 유효성 검사
    private void validateFriendRequest(Member fromMember, Member toMember) {
        if (fromMember == null || toMember == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        if (fromMember.getId().equals(toMember.getId())) {
            throw new IllegalArgumentException("자기 자신과는 친구가 될 수 없습니다.");
        }

        if (areFriends(fromMember, toMember)) {
            throw new IllegalStateException("이미 친구 관계가 존재합니다.");
        }
    }

    // 친구 삭제 권한 검증
    private void validateFriendDeletion(Friend friend, Member requestingMember) {
        if (!friend.getFromMember().getId().equals(requestingMember.getId()) &&
                !friend.getToMember().getId().equals(requestingMember.getId())) {
            throw new IllegalStateException("해당 친구 관계를 삭제할 권한이 없습니다.");
        }
    }
}