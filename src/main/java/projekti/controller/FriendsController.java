package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import projekti.entity.Account;
import projekti.service.AccountService;
import projekti.service.AuthenticationService;
import projekti.service.FriendService;

@Controller
public class FriendsController {
    
    @Autowired
    private FriendService friendService;
    
    @Autowired
    private AccountService accountSearchService; 
    
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/friends")
    public String list(Model model) {
        Account account = authenticationService.getUserAccount();
        model.addAttribute("friends", friendService.findFriendsForAccount(account));
        return "friends";
    }
    
    @PostMapping("/friends/{id}/delete")
    public String delete(@PathVariable long id) {
        Account friend = accountSearchService.findById(id);
        Account account = authenticationService.getUserAccount();
        friendService.deleteFriendship(account, friend);
        return "redirect:/friends";
    }
    
}
