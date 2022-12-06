package practice.springdatajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@ToString(of = {"id", "username", "age"}) // 객체를 찍을때 출력해준다.
@NamedQuery( // 메소드 이름으로 인한 쿼리메소드 방식 말고 NamedQuery 방식이다.
        name = "Member.findByMembername",
        query = "select m from Member m where m.username = :username"
)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY) // team을 가짜 프록시 객체로 가지고있다가 진짜 필요할때 쿼리를 날린다. (ToOne관계는 기본으로 EAGER 조회이다.)
    @JoinColumn(name = "team_id") // 외래키 (다쪽이 항상 연관관계의 주인이며 외래키 관리를 해야한다.)
    private Team team;

    protected Member() { // JPA는 프록시 객체를 생성할때(지연로딩 시) 기본 생성자가 private이면 프록시가 생성이 안되므로 protected로 열어놓는다. public하면 막무가내로 new로 인스턴스를 만들 수 있기 때문에 public은 쓰지 않는다.
    } // 이거 대신 @NoArgsConstructor(access = AccessLevel.PROTECTED)를 써도 된다.

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    // 연관관계 편의 메소드
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
