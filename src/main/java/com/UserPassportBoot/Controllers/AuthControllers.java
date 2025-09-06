package com.UserPassportBoot.Controllers;

import com.UserPassportBoot.services.UserService;
import com.UserPassportBoot.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.UserPassportBoot.model.User;

@Controller
@RequestMapping("/auth")
public class AuthControllers {

    private final UserValidator userValidator;
    private final UserService userService;

    @Autowired
    public AuthControllers(UserValidator userValidator, UserService userService) {
        this.userValidator = userValidator;
        this.userService = userService;
    }
    // страница логина
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    // страница регистрации
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }
    /*
     тут проверяется валидность данных пользователя, описанных в валидаторе и entity user,
      при успешной регистрации идёт перенапрвление на страницу логина
     */
    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        userService.save(user);
        return "redirect:/login";
    }


}