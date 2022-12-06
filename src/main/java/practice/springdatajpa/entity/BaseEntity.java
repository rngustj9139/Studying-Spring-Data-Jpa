package practice.springdatajpa.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter @Setter
public class BaseEntity { // Spring Data Jpa의 Auditing 기능을 이용하기 전에 메인클레스에 @EnableJpaAuditing이라는 어노테이션 추가해야함

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @CreatedBy // 메인 클래스에 빈 추가해야함
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy // 메인 클래스에 빈 추가해야함
    private String lastModifiedBy;

}
