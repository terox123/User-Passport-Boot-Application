package com.UserPassportBoot.DTO;

import com.UserPassportBoot.model.User;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PassportDTO {

    @NotEmpty(message = "Serial can't be empty")
    @Size(min = 4, max = 4, message = "Serial must be 4 digits")
    private String serial;

    @NotEmpty(message = "Number can't be empty")
    @Size(min = 6, max = 6, message = "Number must be 6 digits")
    private String number;

    @OneToOne
    private User owner;



}
