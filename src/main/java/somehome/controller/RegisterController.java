package somehome.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import somehome.entity.Account;
import somehome.repository.AccountRepository;
import somehome.service.AccountService;
import somehome.service.AuthenticationService;

/**
 * Register contoller class.
 */
@Controller
public class RegisterController {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private AccountService accountService;

  /**
   * Get registration page.
   *
   * @param account account
   * @return registration page
   */
  @GetMapping("/register")
  public String registerPage(@ModelAttribute Account account) {
    // if user is authenticated --> redirect to homepage
    if (authenticationService.isAuthenticated()) {
      return "redirect:/home";
    }
    // non authenticated users can proceed
    return "register";
  }

  /**
   * Register to application.
   *
   * @param account       account
   * @param bindingResult binding result
   * @return result
   */
  @PostMapping("/register")
  public String create(
      @Valid @ModelAttribute Account account, BindingResult bindingResult) {
    // if username already exists, add error
    if (accountRepository.findByUsername(account.getUsername()) != null) {
      bindingResult.rejectValue("username", "error.account",
          "Username has already been taken.");
    }
    // if profilename already exists, then back to registeration
    if (accountRepository.findByProfilename(account.getProfilename()) != null) {
      bindingResult.rejectValue("profilename", "error.account",
          "Profilename has already been taken.");
    }
    if (bindingResult.hasErrors()) {
      return "register";
    }
    // parameters were good --> encode the password, save to database and redirect
    // to login page
    accountService.createAccount(account);
    return "redirect:/login";
  }

}
