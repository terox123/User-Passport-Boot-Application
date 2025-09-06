package com.UserPassportBoot.DTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO{

    @Min(value = 0)
    @Max(value = 130)
    private int age;

    @NotBlank(message = "Name can't be empty")
    private String name;

    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Email should be valid")
    private String email;

    @Past(message = "Date should be in past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @NotEmpty(message = "Gender can't be empty")
    private String gender;

    private String password;


    @NotEmpty(message = "Role can't be empty")
    private String role;


}
