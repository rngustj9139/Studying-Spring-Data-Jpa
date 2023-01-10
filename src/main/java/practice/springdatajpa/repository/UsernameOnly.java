package practice.springdatajpa.repository;

import lombok.Value;

public interface UsernameOnly {

//  @Value("#{target.username + ' ' + target.age}") // 이 방식은 open projections이다.
    String getUsername(); // username만 가져온다. (close projections)

}
