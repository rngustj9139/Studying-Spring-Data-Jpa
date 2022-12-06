package practice.springdatajpa.repository;

import practice.springdatajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    List<Member> findMemberCustom();

}
