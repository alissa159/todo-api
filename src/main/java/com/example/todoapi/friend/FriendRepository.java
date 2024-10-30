package com.example.todoapi.friend;

import com.example.todoapi.member.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FriendRepository {

    @PersistenceContext
    private EntityManager em;

    // 생성
    public void save(Friend friend) {
        em.persist(friend);
    }

    // 단건 조회
    public Friend findById(Long friendId) {
        return em.find(Friend.class, friendId);
    }

    // 전체 친구 관계 조회
    public List<Friend> findAll() {
        return em.createQuery("select f from Friend f", Friend.class)
                .getResultList();
    }

    // fromMember의 모든 친구 관계 조회
    public List<Friend> findAllByFromMember(Member fromMember) {
        return em.createQuery("select f from Friend f where f.fromMember = :fromMember", Friend.class)
                .setParameter("fromMember", fromMember)
                .getResultList();
    }

    // toMember의 모든 친구 관계 조회
    public List<Friend> findAllByToMember(Member toMember) {
        return em.createQuery("select f from Friend f where f.toMember = :toMember", Friend.class)
                .setParameter("toMember", toMember)
                .getResultList();
    }

    // 특정 두 회원 간의 친구 관계 조회
    public Friend findByFromMemberAndToMember(Member fromMember, Member toMember) {
        return em.createQuery(
                        "select f from Friend f where f.fromMember = :fromMember and f.toMember = :toMember",
                        Friend.class)
                .setParameter("fromMember", fromMember)
                .setParameter("toMember", toMember)
                .getSingleResult();
    }

    // 삭제
    public void deleteById(Long friendId) {
        Friend friend = findById(friendId);
        em.remove(friend);
    }

    // TEST 용도
    public void flushAndClear() {
        em.flush();
        em.clear();
    }
}