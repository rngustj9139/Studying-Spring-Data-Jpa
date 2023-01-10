package practice.springdatajpa.repository;

public class UsernameOnlyDto { // class 기반 projections

    private final String username;

    public UsernameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
