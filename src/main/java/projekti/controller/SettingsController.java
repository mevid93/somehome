package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.entity.Account;
import projekti.service.AuthenticationService;
import projekti.service.PictureAlbumService;
import projekti.service.SettingsService;

@Controller
public class SettingsController {

    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private SettingsService settingsService;
    
    @Autowired
    private PictureAlbumService pictureAlbumService;

    @GetMapping("/settings")
    public String view(Model model) {
        Account account = authenticationService.getUserAccount();
        model.addAttribute("account", account);
        model.addAttribute("pictures", pictureAlbumService.getPictureAlbumForAccount(account).getPictures());
        return "settings";
    }

    @PostMapping("/settings")
    public String updateSettings(@RequestParam String description, @RequestParam long profilePictureId, Model model) {
        Account account = authenticationService.getUserAccount();
        String errorMessage = settingsService.setAccountSettings(account, description, profilePictureId);
        if (errorMessage != null) {    
            model.addAttribute("account", account);
            model.addAttribute("pictures", account.getPictureAlbum().getPictures());
            return "settings";
        }
        return "redirect:/home";
    }
}
