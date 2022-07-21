package practice.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.springdatajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> { // 인터페이스만 만들면 구현체는 Spring Data Jpa가 다 만들어서 넣어준다. (@Repository 어노테이션 생략 가능)

}
