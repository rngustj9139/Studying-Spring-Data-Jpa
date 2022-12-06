package practice.springdatajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass // 이 엔티티의 프로퍼티를 Member 엔티티가 가지게 될것임
@Getter @Setter
public class JpaBaseEntity {

    // 편하게 엔티티의 생성일, 수정일, 생성자, 수정자를 추적하고싶을때는 아래의 소스를 사용하는 것 보다는 Spring Data Jpa의 Auditing기능 사용
    @Column(updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        this.createdDate = now;
        this.updatedDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

}
