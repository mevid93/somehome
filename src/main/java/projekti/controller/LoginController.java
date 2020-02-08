package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.service.AuthenticationService;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (authenticationService.isAuthenticated()) {
            return "redirect:/home";
        }
        if (error != null) {
            model.addAttribute("errorMessage", "Incorrect Username or Password!");
        }
        return "login";
    }

    @PostMapping("/logout")
    public String logoutPage() {
        return "redirect:/login";
    }

}
