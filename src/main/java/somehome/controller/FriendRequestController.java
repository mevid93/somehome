package somehome.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import somehome.entity.Account;
import somehome.entity.FriendRequest;
import somehome.service.AuthenticationService;
import somehome.service.FriendRequestService;

/**
 * Friend request controller.
 */
@Controller
public class FriendRequestController {

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private FriendRequestService friendRequestService;

  /**
   * Get friend requests.
   *
   * @param model model
   * @return friend requests
   */
  @GetMapping("/friend_requests")
  public String list(Model model) {
    Account account = authenticationService.getUserAccount();
    List<FriendRequest> sentRequests = friendRequestService
        .searchNonAcceptedBySender(account);
    List<FriendRequest> receivedRequests = friendRequestService
        .searchNonAcceptedByReceiver(account);
    model.addAttribute("sent_requests", sentRequests);
    model.addAttribute("received_requests", receivedRequests);
    return "friend_requests";
  }

  @PostMapping("/friend_requests")
  public String create(@RequestParam String profilename) {
    friendRequestService.createNewFriendRequest(profilename);
    return "redirect:/friend_requests";
  }

  @PostMapping("/friend_requests/{id}/delete")
  public String delete(@PathVariable long id) {
    friendRequestService.deleteById(id);
    return "redirect:/friend_requests";
  }

  @PostMapping("/friend_requests/{id}/accept")
  public String accept(@PathVariable long id) {
    friendRequestService.acceptById(id);
    return "redirect:/friend_requests";
  }

}
