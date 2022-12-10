package practice.springdatajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import practice.springdatajpa.entity.Member;
import practice.springdatajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();

        return member.getUsername();
    }

    @GetMapping("/members/{id}")
    public String findMember2(@PathVariable("id") Member member) { // spring data jpa의 도메인 클래스 컨버터 기능 이용(repository를 이용해 엔티티를 찾음, 조회용으로만 사용하기)
        return member.getUsername();
    }

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("userA"));
    }

}
