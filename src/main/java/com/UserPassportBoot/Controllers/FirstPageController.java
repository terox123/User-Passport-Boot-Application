package com.UserPassportBoot.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class FirstPageController {
    // страница home, откуда можно управлять users and passports
    @GetMapping("/")
    public String home() {
        return "first/home";
    }
    // TODO REST controller, в изучении
@ResponseBody
@GetMapping("/api")
public String rest(){
        return "Hello World!";
}

}
