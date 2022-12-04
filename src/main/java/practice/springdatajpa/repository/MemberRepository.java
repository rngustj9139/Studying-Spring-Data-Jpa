package practice.springdatajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import practice.springdatajpa.dto.MemberDto;
import practice.springdatajpa.entity.Member;

import java.util.List;
import java.util.Optional;

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

    @Query("select m from Member m where m.username in :names") // 컬렉션을 파라미터 바인딩
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findListByUsername(String username); // 컬렉션 조회

    Member findMemberByUsername(String username); // 단건 조회

    Optional<Member> findOptionalByUsername(String username); // 단건 조회(Optional)

    Page<Member> findByAge(int age, Pageable pageable); // 페이징 수행, Page는 몇번째 페이지인지 적힌 것을 구현할때 사용, Slice는 더보기 버튼만 구현할때만 사용

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m") // 조인할 경우 totalCount를 구하는 쿼리에서도 조인이 일어나지 않게 분리 countQuery = "~" 안쓰면 성능 저하됨
    Page<Member> findHelloByAge(int age, Pageable pageable); // 페이징 수행, Page는 몇번째 페이지인지 적힌 것을 구현할때 사용, Slice는 더보기 버튼만 구현할때만 사용

//  @Modifying(clearAutomatically = true) // 벌크연산을 한뒤 영속성 컨텍스트를 clear하고 싶을때 사용
    @Modifying // executeUpdate 실행
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age") // 벌크 연산
    int bulkAgePlus(@Param("age") int age); // 변경된 데이터의 수를 반환

    @Query("select m from Member m join fetch m.team") // 그냥 findAll()함수는 N + 1문제가 발생하므로 이 함수는 N + 1 문제를 해결하기 위한 페치 조인을 사용함
    List<Member> findMemberFetchJoin();

    @Override // 메서드 오버라이딩
    @EntityGraph(attributePaths = {"team"}) // 그냥 findAll을 사용할경우 N + 1 문제가 발생하기 때문에 EntityGraph기능을 사용 (페치 조인과 결과 똑같음)
    List<Member> findAll();

}
