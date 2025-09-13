package com.UserPassportBoot.Controllers;


import com.UserPassportBoot.DTO.PassportDTO;
import com.UserPassportBoot.model.Passport;
import com.UserPassportBoot.model.User;
import com.UserPassportBoot.services.PassportService;
import com.UserPassportBoot.services.UserService;
import com.UserPassportBoot.util.PassportValidator;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/passports")
public class PassportController {

    private final PassportService passportService;
    private final UserService userService;
    private final PassportValidator passportValidator;
    private final ModelMapper modelMapper;
    @Autowired
    public PassportController(PassportService passportService,
                              UserService userService,
                              PassportValidator passportValidator, ModelMapper modelMapper) {
        this.passportService = passportService;
        this.userService = userService;
        this.passportValidator = passportValidator;
        this.modelMapper = modelMapper;
    }
    /*
    Возвращаются все паспорта, есть пагинация, тут можно настроить её вручную, также есть сортировка
    по полям объекта паспорта, например, по дате получения, по умолчанию идёт по возрастанию id
     */
    @GetMapping
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "asc") String direction)
    {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<Passport> passportPage = passportService.allPassports(pageable);

        model.addAttribute("passports", passportPage.getContent());
        return "passport/index";
    }

    // информация о паспорте
    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id) {
        model.addAttribute("passport", passportService.showByIdOfPassport(id));
        return "passport/show";
    }
    /*
     в последующих методах контроллера пагинация идёт по дефолту, тут идёт перенаправление
     на страницу создания паспорта, добавляются пустой паспорт, и пользователи для назначения
     */
    @GetMapping("/new")
    public String newPassport(@ModelAttribute("passport") PassportDTO passportDTO,
                              Model model,
                              @PageableDefault() Pageable pageable) {

        model.addAttribute("users", userService.findAllUsers(pageable));
        return "passport/new";
    }
    /*
    идёт проверка на валидность данных, пользователь назначается по id из th:value в passports/new
    и последующее сохранение паспорта, есть проверка на отсутствие пользователя, добавлена на нестандартные случаи
     */
    @PostMapping
    public String save(@ModelAttribute("passport") @Valid PassportDTO passportDTO,
                       BindingResult bindingResult,
                       @RequestParam("ownerId") Integer ownerId,
                       Model model,
                       @PageableDefault() Pageable pageable) {
        Passport passport = convertToPassport(passportDTO);
        passportValidator.validate(passport, bindingResult);

        if (ownerId == null) {
            bindingResult.rejectValue("owner", "error.passport", "Owner must be selected");
            model.addAttribute("users", userService.findAllUsers(pageable));
            return "passport/new";
        }

        User user = userService.findUserById(ownerId);
        if (user.getPassport() != null) {
            bindingResult.rejectValue("owner", "error.passport", "User already has a passport");
            model.addAttribute("users", userService.findAllUsers(pageable));
            return "passport/new";
        }

        passport.setOwner(user);
        passportService.save(passport);
        return "redirect:/passports";
    }

    /*
    перенаправление на страницу изменения данных паспорта, пользователя менять нельзя! передаётся сам паспорта
    и его владелец
     */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,
                       Model model) {
        model.addAttribute("passport", passportService.showByIdOfPassport(id));
        model.addAttribute("owner", passportService.showByIdOfPassport(id).getOwner());
        return "passport/edit";
    }
    /*
    обработка данных на валидность и обновление паспорта
     */
    @PostMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("passport") @Valid Passport passport,
                         BindingResult bindingResult) {

        passportValidator.validate(passport, bindingResult);

        if(bindingResult.hasErrors()){
            return "passport/edit";
        }
        Passport existingPassport = passportService.showByIdOfPassport(id);
        passport.setOwner(existingPassport.getOwner());

        passportService.update(id, passport);
        return "redirect:/passports";
    }
    // перенаправление на страницу удаления паспорта
    @GetMapping("/{id}/delete")
    public String deletePassport(@PathVariable("id") int id, Model model) {
        Passport passport = passportService.showByIdOfPassport(id);
        model.addAttribute("passport", passport);
        model.addAttribute("owner", passport.getOwner());
        return "passport/delete";
    }
    // удаление паспорта
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        passportService.delete(id);
        return "redirect:/passports";
    }
    // перенаправление на страницу поиска паспорта по номеру
    @GetMapping("/search")
    public String searchPage() {
        return "passport/search";
    }
    // метод поиска реализован через репозиторий, поиск осуществляется через requestParam в форме
    @GetMapping("/search/results")
    public String searchResult(Model model,
                               @RequestParam("numberPart") String partOfNumber,
                               @PageableDefault() Pageable pageable) {

        Page<Passport> passportPage = passportService.searchPassportByStartingWith(partOfNumber, pageable);

        model.addAttribute("passportsByPart", passportPage.getContent());
        model.addAttribute("numberPart", partOfNumber);

        return "passport/searchResult";
    }

private Passport convertToPassport(PassportDTO passportDTO){
        return modelMapper.map(passportDTO, Passport.class);
}

}