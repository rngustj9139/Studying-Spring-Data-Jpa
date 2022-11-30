package practice.springdatajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

}
