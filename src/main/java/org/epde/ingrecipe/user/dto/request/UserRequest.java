package org.epde.ingrecipe.user.dto.request;

import lombok.Data;
import org.epde.ingrecipe.auth.role.model.Role;

@Data
public class UserRequest {

    private String username;
    private String email;
    private String password;
    private Role role;

}
