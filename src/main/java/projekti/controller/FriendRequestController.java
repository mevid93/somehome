package projekti.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.entity.Account;
import projekti.entity.FriendRequest;
import projekti.service.AuthenticationService;
import projekti.service.FriendRequestService;

@Controller
public class FriendRequestController {

    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private FriendRequestService friendRequestService;

    @GetMapping("/friend_requests")
    public String list(Model model) {
        Account account = authenticationService.getUserAccount();
        List<FriendRequest> sent_requests = friendRequestService.searchNonAcceptedBySender(account);
        List<FriendRequest> received_requests = friendRequestService.searchNonAcceptedByReceiver(account);
        model.addAttribute("sent_requests", sent_requests);
        model.addAttribute("received_requests", received_requests);
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
