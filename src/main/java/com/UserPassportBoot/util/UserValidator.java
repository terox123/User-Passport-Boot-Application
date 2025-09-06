package com.UserPassportBoot.util;


import com.UserPassportBoot.model.User;
import com.UserPassportBoot.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        LocalDate dateToday = LocalDate.now();
        LocalDate date14YearsAgo = dateToday.minusYears(14);
        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null && existingUser.getId() != user.getId()) {
            errors.rejectValue("email", "", "Email is already exists");
        }

        if(user.getName() == null || !user.getName().matches("^[a-zA-Z]{2,20}$")){
            errors.rejectValue("name", "", "Name should be from 2 to 20 characters, without numbers");
        }

        if (user.getDateOfBirth() == null) {
            errors.rejectValue("dateOfBirth", "", "Date of birth is required");
        }

        if (user.getId() == 0 && (user.getPassword() == null || user.getPassword().isEmpty())) {
            errors.rejectValue("password", "", "Password is required");
        }
        if(!(user.getDateOfBirth().isEqual(date14YearsAgo) || user.getDateOfBirth().isBefore(date14YearsAgo))){
    errors.rejectValue("dateOfBirth", "", "User can be 14 year person!");
}
    }



}
