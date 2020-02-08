package projekti.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.entity.Account;
import projekti.service.AccountService;
import projekti.service.AuthenticationService;
import projekti.service.FriendService;
import projekti.service.MessageService;
import projekti.service.PictureService;

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

    @GetMapping("/home")
    public String myPage(Model model) {
        Account account = authenticationService.getUserAccount();
        model.addAttribute("account", account);
        model.addAttribute("profile_picture", pictureService.getAccountProfilePicture(account));
        model.addAttribute("messages", messageService.getLatestWallMessages(account));
        return "index";
    }

    @GetMapping("/accounts")
    public String list(@RequestParam(value = "usersearchfield", required = false) String usersearchfield, Model model) {
        usersearchfield = usersearchfield == null ? "" : usersearchfield.trim();
        List<Account> accounts = accountSearchService.listByProfilename(usersearchfield);
        model.addAttribute("accounts", accounts);
        return "accounts";
    }

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
