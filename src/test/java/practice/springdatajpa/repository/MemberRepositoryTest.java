package practice.springdatajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import practice.springdatajpa.entity.Member;

import java.util.List;
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

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findedMember1 = memberRepository.findById(member1.getId()).get();
        Member findedMember2 = memberRepository.findById(member2.getId()).get();

        // 단건 조회 검증
        Assertions.assertThat(findedMember1).isEqualTo(member1);
        Assertions.assertThat(findedMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        // 개수 조회 검증
        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);

    }

}