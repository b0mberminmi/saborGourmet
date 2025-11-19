package cl.ipss.saborGourmet.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class loginController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

}
