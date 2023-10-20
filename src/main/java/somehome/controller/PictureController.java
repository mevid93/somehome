package somehome.controller;

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
import somehome.entity.Account;
import somehome.entity.Picture;
import somehome.service.AuthenticationService;
import somehome.service.FriendService;
import somehome.service.PictureService;

/**
 * Picture controller class.
 */
@Controller
public class PictureController {

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private PictureService pictureService;

  @Autowired
  private FriendService friendService;

  /**
   * Get pictures.
   *
   * @param model model
   * @return pictures
   */
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

  /**
   * Get picture by id.
   *
   * @param id    picture id
   * @param model model
   * @return picture
   */
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

  /**
   * Add picture to album.
   *
   * @param file        file
   * @param description description
   * @param model       model
   * @return result
   * @throws IOException exception
   */
  @PostMapping("/pictures")
  public String add(
      @RequestParam("picture") MultipartFile file, @RequestParam String description, Model model)
      throws IOException {
    Account account = authenticationService.getUserAccount();
    String errorMessage = pictureService.addPictureForAccount(account, file, description);
    if (errorMessage != null) {
      model.addAttribute("errorMessage", errorMessage);
      model.addAttribute("pictures", pictureService.getPicturesByAccount(account));
      return "pictures";
    }
    return "redirect:/pictures";
  }

  /**
   * Like picture.
   *
   * @param id picture id
   * @return redirect
   */
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

  /**
   * Comment a picture.
   *
   * @param id id
   * @param comment comment
   * @return redirect
   */
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

  /**
   * delete a picture.
   *
   * @param id picture id
   * @return redirect
   */
  @PostMapping("/pictures/{id}/delete")
  public String delete(@PathVariable long id) {
    Account account = authenticationService.getUserAccount();
    pictureService.deleteUsersPictureById(account, id);
    return "redirect:/pictures";
  }

}
