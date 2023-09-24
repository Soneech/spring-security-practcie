package org.soneech.springcourse.dto;

import lombok.Getter;
import lombok.Setter;
import org.soneech.springcourse.model.Role;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private int yearOfBirth;
    private Role role;
}
