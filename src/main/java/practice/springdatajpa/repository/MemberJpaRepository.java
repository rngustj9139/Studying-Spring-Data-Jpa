package practice.springdatajpa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import practice.springdatajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext // @RequiredArgsConstruct와 private final없이 EntityManager를 주입해주는 어노테이션이다.
    private EntityManager em;

    public Member save(Member member) {
        em.persist(member);

        return member;
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        List<Member> result = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        return result;
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);

        return Optional.ofNullable(member);
    }

    public long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public List<Member> findByPage(int age, int offset, int limit) { // 순수 JPA 페이징과 정렬
        return em.createQuery("select m from Member m where m.age = :age order by m.username desc", Member.class)
                    .setParameter("age", age)
                    .setFirstResult(offset) // 어디서부터 시작할 것인가
                    .setMaxResults(limit)
                    .getResultList(); // 몇개 가져올 것인가
    }

    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    public int bulkAgePlus(int age) { // 벌크 연산
        return em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate(); // 변경된 칼럼 수가 리턴된다.
    }

}
