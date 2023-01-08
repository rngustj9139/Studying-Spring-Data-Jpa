package practice.springdatajpa.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Item implements Persistable<String> { // id를 직접 할당해서 넣는 경우가 생길 수 있다. persist는 엔티티가 id가 없는 경우(처음인 경우)에 수행되고 merge는 아닌 경우 수행된다. (merge는 select 쿼리가 나가기 때문에 비효율적 => detach된 상태에서만 사용해야함)

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return (createdDate == null);
    }

}
