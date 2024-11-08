package org.rest.restapp.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class UserDTO {

    private Integer id;

    @NotBlank
    @Size(min = 7, max = 255)
    private String name;

    @NotBlank
    @Size(min = 7, max = 255)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;
}
