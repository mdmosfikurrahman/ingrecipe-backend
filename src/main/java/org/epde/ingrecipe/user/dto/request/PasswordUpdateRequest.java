package org.epde.ingrecipe.user.dto.request;

import lombok.Data;

@Data
public class PasswordUpdateRequest {
    private String email;
    private String newPassword;
}
