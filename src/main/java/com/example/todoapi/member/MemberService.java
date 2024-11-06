package com.example.todoapi.member;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 가입
    @Transactional
    public Long join(String username) {
        validateUsername(username);
        validateDuplicateUsername(username);

        Member member = new Member(username);
        memberRepository.save(member);
        return member.getId();
    }

    // 로그인
    public Member login(String username) {
        try {
            return memberRepository.findByUsername(username);
        } catch (NoResultException e) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
    }

    // 회원 단건 조회
    public Member getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            throw new NoResultException("해당 회원을 찾을 수 없습니다. ID: " + memberId);
        }
        return member;
    }

    // username으로 회원 조회
    public Member getMemberByUsername(String username) {
        try {
            return memberRepository.findByUsername(username);
        } catch (NoResultException e) {
            throw new NoResultException("해당 회원을 찾을 수 없습니다. Username: " + username);
        }
    }

    // 전체 회원 조회
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // 회원 삭제
    @Transactional
    public void deleteMember(Long memberId) {
        Member member = getMemberById(memberId);
        memberRepository.deleteById(memberId);
    }

    // 아이디 중복 검사
    private void validateDuplicateUsername(String username) {
        try {
            Member findMember = memberRepository.findByUsername(username);
            if (findMember != null) {
                throw new IllegalStateException("이미 존재하는 회원입니다.");
            }
        } catch (NoResultException e) {
            // username이 중복되지 않은 경우 (정상)
        }
    }

    // username 유효성 검사
    private void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("사용자 이름은 비워둘 수 없습니다.");
        }
        if (username.length() < 3 || username.length() > 20) {
            throw new IllegalArgumentException("사용자 이름은 3자 이상 20자 이하여야 합니다.");
        }
        // 추가적인 유효성 검사 규칙을 여기에 구현할 수 있습니다.
        // 예: 특수문자 제한, 영문/숫자만 허용 등
    }
}