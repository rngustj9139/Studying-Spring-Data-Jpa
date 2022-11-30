package practice.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import practice.springdatajpa.dto.MemberDto;
import practice.springdatajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> { // 인터페이스만 만들면 구현체는 Spring Data Jpa가 다 만들어서 넣어준다. (@Repository 어노테이션 생략 가능)

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age); // (커스텀 기능 개발 - 쿼리 메서드) 구현하지 않아도 Spring Data Jpa가 자동으로 구현해줌 => 메서드 이름으로 쿼리 생성

    List<Member> findHelloBy(); // 다 조회한다. find...By

    List<Member> findTop3HelloBy(); // 3개만 조회한다. find...By

    @Query(name = "Member.findByMembername") // 이 줄 주석처리해도 동작 잘 된다.
    List<Member> findByMembername(@Param("username") String username); // named Query 방식이다 => Member 엔티티에 어노테이션 추가해야한다. (이 기능은 실무에 거의 안쓰인다.) (jpql에 오타 있으면 에플리케이션 로딩시점에 잡아준다.)

    @Query("select m from Member m where m.username = :username and m.age = :age") // jpql에 오타 있으면 에플리케이션 로딩시점에 잡아준다.
    List<Member> findUser(@Param("username") String username, @Param("age") int age); // @Query 방법 (리포지토리 메소드에 쿼리 정의하기) => 이 기능을 실무에서 많이 쓴다.

    @Query("select m.username from Member m") // 단순 값 조회
    List<String> findUsernameList();

    @Query("select new practice.springdatajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t") // dto로 조회
    List<MemberDto> findMemberDto();

}
