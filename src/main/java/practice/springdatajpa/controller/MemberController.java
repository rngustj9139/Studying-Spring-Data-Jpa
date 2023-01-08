package practice.springdatajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import practice.springdatajpa.dto.MemberDto;
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

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) { // spring data jpa의 도메인 클래스 컨버터 기능 이용(repository를 이용해 엔티티를 찾음, 조회용으로만 사용하기)
        return member.getUsername();
    }

    @GetMapping("/members") // localhost:8080/members?page=0 하면 데이터를 20개 까지만 가져온다. localhost:8080/members/page=0&size=3 하면 3개만 가져온다. localhost:8080/members?page=0&size=3&sort=id.desc 도 가능
    public Page<Member> list(Pageable pageable) { // application.yml에서 pageable 사이즈 전역설정 가능
        Page<Member> page = memberRepository.findAll(pageable);

        return page;
    }

    @GetMapping("/members2")
    public Page<MemberDto> list2(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        return map;
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("member" + i));
        }
    }

}
