package somehome.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import somehome.entity.Account;
import somehome.service.AccountService;
import somehome.service.AuthenticationService;
import somehome.service.FriendService;
import somehome.service.MessageService;
import somehome.service.PictureService;

/**
 * Account controller class.
 */
@Controller
public class AccountController {

  @Autowired
  private AccountService accountSearchService;

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private FriendService friendService;

  @Autowired
  private PictureService pictureService;

  @Autowired
  private MessageService messageService;

  /**
   * Get home page.
   *
   * @param model model
   * @return home page
   */
  @GetMapping("/home")
  public String myPage(Model model) {
    Account account = authenticationService.getUserAccount();
    model.addAttribute("account", account);
    model.addAttribute("profile_picture", pictureService.getAccountProfilePicture(account));
    model.addAttribute("messages", messageService.getLatestWallMessages(account));
    return "index";
  }

  /**
   * Get list of accounts.
   *
   * @param usersearchfield search key word
   * @param model           model
   * @return accounts
   */
  @GetMapping("/accounts")
  public String list(
      @RequestParam(value = "usersearchfield", required = false) 
      String usersearchfield, Model model) {
    usersearchfield = usersearchfield == null ? "" : usersearchfield.trim();
    List<Account> accounts = accountSearchService.listByProfilename(usersearchfield);
    model.addAttribute("accounts", accounts);
    return "accounts";
  }

  /**
   * Find account by profilename.
   *
   * @param profilename profile name
   * @param model       model
   * @return result
   */
  @GetMapping("/accounts/{profilename}")
  public String search(@PathVariable String profilename, Model model) {
    Account account = accountSearchService.findByProfilename(profilename.trim());
    if (account == null) {
      return "redirect:/accounts";
    }
    if (account.getUsername().equals(authenticationService.getUsername())) {
      return "redirect:/home";
    }
    Account user = authenticationService.getUserAccount();
    model.addAttribute("friend_status", friendService.areAccountsFriends(user, account));
    model.addAttribute("account", account);
    model.addAttribute("profile_picture", pictureService.getAccountProfilePicture(account));
    model.addAttribute("messages", messageService.getLatestWallMessages(account));
    return "account";
  }

}
