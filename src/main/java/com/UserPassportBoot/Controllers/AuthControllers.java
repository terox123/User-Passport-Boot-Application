package com.UserPassportBoot.Controllers;

import com.UserPassportBoot.DTO.UserDTO;
import com.UserPassportBoot.services.UserService;
import com.UserPassportBoot.util.UserValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
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
private final ModelMapper modelMapper;
    @Autowired
    public AuthControllers(UserValidator userValidator, UserService userService, ModelMapper modelMapper) {
        this.userValidator = userValidator;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }
    // страница логина
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    // страница регистрации
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") UserDTO userDTO) {
        return "auth/registration";
    }

    /*
     тут проверяется валидность данных пользователя, описанных в валидаторе и entity user,
      при успешной регистрации идёт перенаправление на страницу логина
     */

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid UserDTO userDTO,
                                      BindingResult bindingResult) {
        User user = convertToUser(userDTO);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        userService.save(user);
        return "redirect:/login";
    }

private User convertToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
}
}