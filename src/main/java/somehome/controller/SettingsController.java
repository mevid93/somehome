package somehome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import somehome.entity.Account;
import somehome.service.AuthenticationService;
import somehome.service.PictureAlbumService;
import somehome.service.SettingsService;

/**
 * Settings controller class.
 */
@Controller
public class SettingsController {

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private SettingsService settingsService;

  @Autowired
  private PictureAlbumService pictureAlbumService;

  /**
   * Get settings view.
   *
   * @param model model
   * @return settings view
   */
  @GetMapping("/settings")
  public String view(Model model) {
    Account account = authenticationService.getUserAccount();
    model.addAttribute("account", account);
    var album = pictureAlbumService.getPictureAlbumForAccount(account);
    model.addAttribute("pictures", album.getPictures());
    return "settings";
  }

  /**
   * Update settings.
   *
   * @param description      description
   * @param profilePictureId profile picture id
   * @param model            model
   * @return result
   */
  @PostMapping("/settings")
  public String updateSettings(
      @RequestParam String description, @RequestParam long profilePictureId, Model model) {
    Account account = authenticationService.getUserAccount();
    String errorMessage = settingsService
        .setAccountSettings(account, description, profilePictureId);
    if (errorMessage != null) {
      model.addAttribute("account", account);
      model.addAttribute("pictures", account.getPictureAlbum().getPictures());
      return "settings";
    }
    return "redirect:/home";
  }
}
