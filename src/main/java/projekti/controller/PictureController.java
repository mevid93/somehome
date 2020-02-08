package projekti.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import projekti.entity.Account;
import projekti.entity.Picture;
import projekti.service.AuthenticationService;
import projekti.service.FriendService;
import projekti.service.PictureService;

@Controller
public class PictureController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private FriendService friendService;

    @GetMapping("/pictures")
    public String list(Model model) {
        Account account = authenticationService.getUserAccount();
        model.addAttribute("account", account);
        model.addAttribute("pictures", pictureService.getPicturesByAccount(account));
        return "pictures";
    }

    @GetMapping("/pictures/{id}")
    @ResponseBody
    public byte[] get(@PathVariable long id) {
        return pictureService.getPictureContentByPictureId(id);
    }

    @GetMapping("/pictures/{id}/view")
    public String viewPicture(@PathVariable long id, Model model) {
        Account account = authenticationService.getUserAccount();
        Picture picture = pictureService.getPictureById(id);
        if (picture == null) {
            return "redirect:/home";
        }
        Account owner = pictureService.getPictureOwner(picture);
        // check if account is friend with owner or that account is owner
        int status = friendService.areAccountsFriends(account, owner);
        status = (account.getId() == owner.getId()) ? 2 : status; 
        model.addAttribute("comments", pictureService.getLatestComments(picture));
        model.addAttribute("picture", picture);
        model.addAttribute("account", owner);
        model.addAttribute("friend_status", status);
        return "picture";
    }

    @PostMapping("/pictures")
    public String add(@RequestParam("picture") MultipartFile file, @RequestParam String description, Model model) throws IOException {
        Account account = authenticationService.getUserAccount();
        String errorMessage = pictureService.addPictureForAccount(account, file, description);
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("pictures", pictureService.getPicturesByAccount(account));
            return "pictures";
        }
        return "redirect:/pictures";
    }

    @PostMapping("/pictures/{id}/like")
    public String like(@PathVariable long id) {
        Account account = authenticationService.getUserAccount();
        Picture picture = pictureService.getPictureById(id);
        pictureService.createLike(account, picture);
        if (picture == null) {
            return "redirect:/home";
        }
        return "redirect:/pictures/" + picture.getId() + "/view";
    }

    @PostMapping("/pictures/{id}/comment")
    public String comment(@PathVariable long id, @RequestParam String comment) {
        Account account = authenticationService.getUserAccount();
        Picture picture = pictureService.getPictureById(id);
        pictureService.createComment(account, picture, comment);
        if (picture == null) {
            return "redirect:/home";
        }
        return "redirect:/pictures/" + picture.getId() + "/view";
    }

    @PostMapping("/pictures/{id}/delete")
    public String delete(@PathVariable long id) {
        Account account = authenticationService.getUserAccount();
        pictureService.deleteUsersPictureById(account, id);
        return "redirect:/pictures";
    }

}
