package practice.springdatajpa.repository;

public interface NestedProjections {

    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }

}
