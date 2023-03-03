package com.aminnorouzi.ms.model.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    private String username;

    @ToString.Exclude
    private String password;

    private String fullName;
    private Boolean authenticated;
    private Boolean automated;

    public static UserRequest of(String username, String password) {
        return new UserRequest(username, password, null, false, false);
    }
}
