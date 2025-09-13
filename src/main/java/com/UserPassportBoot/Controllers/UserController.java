package com.UserPassportBoot.Controllers;



import com.UserPassportBoot.DTO.UserDTO;
import com.UserPassportBoot.model.Passport;
import com.UserPassportBoot.model.User;
import com.UserPassportBoot.services.PassportService;
import com.UserPassportBoot.services.UserService;
import com.UserPassportBoot.util.UserValidator;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.sql.SQLOutput;


@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final PassportService passportService;
    private final ModelMapper modelMapper;
    @Autowired
    public UserController(UserService userService, UserValidator userValidator, PassportService passportService, ModelMapper modelMapper) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.passportService = passportService;
        this.modelMapper = modelMapper;

    }
    /*
    отображение всех пользователей, кастомная пагинация только в этом методе, сортировка через RequestParam по
    полям сущности User, также есть кастомная сортировка по возрасту
     */
    @GetMapping
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false) String sort) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> usersPage = switch (sort == null ? "" : sort) {
            case "old" -> userService.findAllUsersSortedByBirthDateAsc(pageable);
            case "young" -> userService.findAllUsersSortedByBirthDateDesc(pageable);
            default -> userService.findAllUsers(pageable);
        };

        model.addAttribute("users", usersPage.getContent());


        return "user/index";
    }
    // отображение пользователя, его данных
    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id, @ModelAttribute("passport") Passport passport){
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("passport", passportService.findByOwner(userService.findUserById(id)));
        return "user/show";
    }
    // перенаправление на страницу создания user
    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") UserDTO userDTO){
        return "user/new";
    }
/*
 проверка валидности данных, и последующее сохранение при правильно, введённых данных, иначе
 пользователь остаётся на этой же странице
 */

    @PostMapping()
    public String create(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult){

        User user = convertToUser(userDTO);
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors()){
            return "user/new";
        }
        userService.save(user);
        return "redirect:/users";
    }
    /*
    перенаправление на страницу изменения данных пользователя, передаётся в модель также сам user по id
     */
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("user", userService.findUserById(id));
        return "user/edit";
    }
// проверка на валидность данных, при правильных изменениях redirect на страницу с пользователями
//, иначе остаётся на этой же станице

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         @PathVariable("id") int id,
                         BindingResult bindingResult){
        userValidator.validate(user, bindingResult);
        if(bindingResult.hasErrors())
            return "user/edit";

        user.setId(id);
        userService.update(user.getId(), user);
        return "redirect:/users";

    }
    // перенаправление на страницу удаления пользователя
    @GetMapping("/{id}/delete")
    public String delete(Model model, @PathVariable("id") int id){
        model.addAttribute("user", userService.findUserById(id));
        return "user/delete";
    }
    // удаление пользователя
    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") int id){
        userService.delete(id);
        return "redirect:/users";
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public String noPassportError(EntityNotFoundException ex, Model model){
        model.addAttribute("error", ex.getMessage());
        return "user/show";
    }


    private User convertToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);

    }

    private UserDTO convertToUserDto(User user){
        return modelMapper.map(user, UserDTO.class);
    }

}