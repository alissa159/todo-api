package com.example.todoapi.member;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    // 생성
    public void save(Member member) {
        em.persist(member);
    }

    // 단건 조회
    public Member findById(Long memberId) {
        return em.find(Member.class, memberId);
    }

    // username으로 회원 찾기
    public Member findByUsername(String username) {
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    // 전체 회원 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 삭제
    public void deleteById(Long memberId) {
        Member member = findById(memberId);
        em.remove(member);
    }

    // TEST 용도
    public void flushAndClear() {
        em.flush();
        em.clear();
    }
}