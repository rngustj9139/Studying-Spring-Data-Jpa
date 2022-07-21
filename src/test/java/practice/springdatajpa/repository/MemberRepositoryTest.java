package practice.springdatajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import practice.springdatajpa.entity.Member;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testMember() {
        Member member = new Member("memberA");

        Member savedMember = memberRepository.save(member);
        Optional<Member> finded = memberRepository.findById(savedMember.getId()); // 있을 수 도 있고 없을 수 도 있기 때문에 optional로 반환이 된다.
        Member findedMember = finded.get();

        Assertions.assertThat(findedMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findedMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findedMember).isEqualTo(member);
    }

}