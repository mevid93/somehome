package projekti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("*")
    public String handleDefault() {
        return "redirect:/home";
    }
    
    @GetMapping("/home/*")
    public String handleHomeDefault() {
        // this is because authenticated user has home page in path /home
        return "redirect:/home";
    }
}
