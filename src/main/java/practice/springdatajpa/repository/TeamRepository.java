package practice.springdatajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import practice.springdatajpa.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

}
