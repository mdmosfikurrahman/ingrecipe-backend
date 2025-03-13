package org.epde.ingrecipe.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String lastLogin;

}
