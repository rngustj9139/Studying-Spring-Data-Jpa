package practice.springdatajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import practice.springdatajpa.entity.Member;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링 빈을 주입해서 테스트를 할것이기 때문에 이 어노테이션을 선언한다 참고로 JUnit5부터는 RunWith를 명시하지 않아도 된다.
@Transactional // 테스트 끝나면 다 롤백해버림 @RollBacl(false) 붙이면 롤백 안함 커밋을 해버림
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() { // @Transactional이 없으면 에러가 난다.
        Member member = new Member("memberA");

        Member savedMember = memberJpaRepository.save(member);
        Member findedMember = memberJpaRepository.find(savedMember.getId());

        Assertions.assertThat(findedMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findedMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findedMember).isEqualTo(member); // findedMember == member
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findedMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findedMember2 = memberJpaRepository.findById(member2.getId()).get();

        // 단건 조회 검증
        Assertions.assertThat(findedMember1).isEqualTo(member1);
        Assertions.assertThat(findedMember2).isEqualTo(member2);

        // 리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        // 개수 조회 검증
        long count = memberJpaRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deletedCount = memberJpaRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void paging() {
        // given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        // when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // 페이지 계산 공식 적용...
        // totalPage = totalCount / size...
        // 마지막 페이지...
        // 최초 페이지...

        // then
        Assertions.assertThat(members.size()).isEqualTo(3);
        Assertions.assertThat(totalCount).isEqualTo(5);
    }

    @Test
    public void bulkUpdate() {
        // given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 19));
        memberJpaRepository.save(new Member("member3", 20));
        memberJpaRepository.save(new Member("member4", 21));
        memberJpaRepository.save(new Member("member5", 40));

        // when
        int resultCount = memberJpaRepository.bulkAgePlus(20);

        // then
        Assertions.assertThat(resultCount).isEqualTo(3);
    }

}