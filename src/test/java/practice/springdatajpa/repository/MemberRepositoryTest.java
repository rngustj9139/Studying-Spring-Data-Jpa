package practice.springdatajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import practice.springdatajpa.dto.MemberDto;
import practice.springdatajpa.entity.Member;
import practice.springdatajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

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

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void nameQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findedMember = result.get(0);
        Assertions.assertThat(findedMember).isEqualTo(m1);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        Member findedMember = result.get(0);
        Assertions.assertThat(findedMember).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        
        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("s = " + s);
        }
    }

    @Test
    public void findMemberDto() {
        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);

        Team team = new Team("teamA");
        m1.setTeam(team);
        teamRepository.save(team);

        memberRepository.findMemberDto();
        List<MemberDto> memberDto = memberRepository.findMemberDto();

        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        
        List<Member> byNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        for (Member byName : byNames) {
            System.out.println("byName = " + byName);    
        }
    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa1 = memberRepository.findListByUsername("AAA");
        for (Member member : aaa1) {
            System.out.println("member = " + member);
        }
        Member aaa2 = memberRepository.findMemberByUsername("AAA");
        System.out.println("aaa2 = " + aaa2);
        Optional<Member> aaa = memberRepository.findOptionalByUsername("AAA");
        System.out.println("aaa.get() = " + aaa.get());
    }

    @Test
    public void paging() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        // Spring Data Jpa는 페이지가 1부터 시작하는 것이 아닌 0부터 시작한다.
        // PageRequest의 부모가 Pageable이다.
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        int age = 10;

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest); // totalCount는 Page를 반환받으면 직접 구할 필요가 없다.
//      page.map(member -> new MemberDto(member.getId(), member.getUsername()));

        // then
        List<Member> content = page.getContent();
//      Slice<Member> content = page.getContent();
        long totalElements = page.getTotalElements(); // totalCount 구하기 (Slice는 totalCount를 구하는 기능 없음)
        int number = page.getNumber(); // 페이지 번호 가져오기
        int totalPages = page.getTotalPages();// 전체 페이지 개수 구하기 (Slice는 totalCount를 구하는 기능 없음)

        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(totalElements).isEqualTo(5);
        Assertions.assertThat(number).isEqualTo(0);
        Assertions.assertThat(totalPages).isEqualTo(2);
        Assertions.assertThat(page.isFirst()).isTrue(); // 첫번째 페이지인지 알아내기
        Assertions.assertThat(page.hasNext()).isTrue(); // 다음 페이지가 있는지 알아내기
    }

    @Test
    public void bulkUpdate() {
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when
        int resultCount = memberRepository.bulkAgePlus(20);

        // then
        Assertions.assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member.getUsername() = " + member.getUsername());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName()); // N + 1 문제 발생
        }
    }

    @Test
    public void queryHint() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member1.getId()).get();
        // 변경 감지로 flush할때 업데이트 쿼리가 나감 but 1차캐시에 원본엔티티와 변경된엔티티 두개가 들어가있기 때문에 메모리를 더 먹음 but 최적화 가능(읽기용으로만 쓴다고 jpa hint 이용)
        findMember.setUsername("member2");
        em.flush();
    }

    @Test
    public void queryHint2() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());
        // 변경 감지로 flush할때 업데이트 쿼리가 나감 but 1차캐시에 원본엔티티와 변경된엔티티 두개가 들어가있기 때문에 메모리를 더 먹음 but 최적화 가능(읽기용으로만 쓴다고 jpa hint 이용)
        findMember.setUsername("member2"); // readOnly로 세팅했기 때문에 변경감지가 작동하지 않는다.
        em.flush();
    }

    @Test
    public void Lock() {
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        List<Member> result = memberRepository.findLockByUsername(member1.getUsername());
    }
    
    @Test
    public void callCustom() {
        List<Member> memberCustom = memberRepository.findMemberCustom();

        for (Member member : memberCustom) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void projections() {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1");

        for (UsernameOnly usernameOnly : result) {
            System.out.println("usernameOnly = " + usernameOnly);
        }
    }

    @Test
    public void nativeQuery() {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Member result = memberRepository.findByNativeQuery("m1");
        System.out.println("result = " + result);
    }

}