package illcaro.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
public class AuthRequestDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;

}

