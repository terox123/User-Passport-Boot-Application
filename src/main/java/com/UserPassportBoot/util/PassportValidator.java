package com.UserPassportBoot.util;


import com.UserPassportBoot.model.Passport;
import com.UserPassportBoot.repositories.PassportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PassportValidator implements Validator {

private final PassportRepository passportRepository;
   @Autowired
    public PassportValidator(PassportRepository passportRepository) {
       this.passportRepository = passportRepository;
   }

    @Override
    public boolean supports(Class<?> clazz) {
        return Passport.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Passport passport = (Passport) target;

        if (passport.getOwner() == null) {
            errors.rejectValue("owner", "owner.required", "Owner must be selected");
        }

        if (passport.getSerial() == null || passport.getSerial().isEmpty()) {
            errors.rejectValue("serial", "NotEmpty.passport.serial", "Serial of passport can't be empty");
        } else if (!passport.getSerial().matches("\\d{4}")) {
            errors.rejectValue("serial", "Pattern.passport.serial", "Serial of passport should have 4 numbers");
        }

        if (passport.getNumber() == null || passport.getNumber().isEmpty()) {
            errors.rejectValue("number", "NotEmpty.passport.number", "Number of passport can't be empty");
        } else if (!passport.getNumber().matches("\\d{6}")) {
            errors.rejectValue("number", "Pattern.passport.number", "Number of passport should have 6 numbers");
        }


        if (passport.getSerial() != null && passport.getNumber() != null) {
            Optional<Passport> existingPassport = passportRepository.findBySerialAndNumber(
                    passport.getSerial(), passport.getNumber());

            if (existingPassport.isPresent()) {
                if (passport.getId() == 0 || existingPassport.get().getId() == (passport.getId())) {
                    errors.rejectValue("serial", "NotUnique.passport",
                            "Паспорт с такой серией и номером уже существует");
                    errors.rejectValue("number", "NotUnique.passport",
                            "Паспорт с такой серией и номером уже существует");
                }
            }
        }




    }
}
