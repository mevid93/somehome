package somehome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import somehome.entity.Account;
import somehome.service.AccountService;
import somehome.service.AuthenticationService;
import somehome.service.FriendService;

/**
 * Friends controller class.
 */
@Controller
public class FriendsController {

  @Autowired
  private FriendService friendService;

  @Autowired
  private AccountService accountSearchService;

  @Autowired
  private AuthenticationService authenticationService;

  /**
   * Get friends.
   *
   * @param model model
   * @return friends
   */
  @GetMapping("/friends")
  public String list(Model model) {
    Account account = authenticationService.getUserAccount();
    model.addAttribute("friends", friendService.findFriendsForAccount(account));
    return "friends";
  }

  /**
   * Remove friend.
   *
   * @param id frend user id
   * @return redirect
   */
  @PostMapping("/friends/{id}/delete")
  public String delete(@PathVariable long id) {
    Account friend = accountSearchService.findById(id);
    Account account = authenticationService.getUserAccount();
    friendService.deleteFriendship(account, friend);
    return "redirect:/friends";
  }

}
