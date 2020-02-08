package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import projekti.entity.Account;
import projekti.entity.PictureAlbum;
import projekti.service.AuthenticationService;
import projekti.service.PictureAlbumService;

@Controller
public class PictureAlbumController {

    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private PictureAlbumService pictureAlbumService;

    @GetMapping("/picture_album")
    public String show(Model model) {
        Account account = authenticationService.getUserAccount();
        model.addAttribute("account", account);
        model.addAttribute("users_album", true);
        model.addAttribute("picture_album", pictureAlbumService.getPictureAlbumForAccount(account));
        return "picture_album";
    }

    @GetMapping("/picture_album/{id}")
    public String showOther(@PathVariable long id, Model model) {
        PictureAlbum pictureAlbum = pictureAlbumService.getPictureAlbumById(id);
        if(pictureAlbum.getOwner().getUsername().equals(authenticationService.getUsername())) {
            return "redirect:/picture_album";
        }
        model.addAttribute("account", pictureAlbum.getOwner());
        model.addAttribute("users_album", false);
        model.addAttribute("picture_album", pictureAlbum);
        return "picture_album";
    }
}
