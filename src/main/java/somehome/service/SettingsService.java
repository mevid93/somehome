package somehome.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import somehome.entity.Account;
import somehome.entity.Picture;
import somehome.repository.AccountRepository;
import somehome.repository.PictureRepository;

/**
 * Settings service class.
 */
@Service
public class SettingsService {

  @Autowired
  private PictureRepository pictureRepository;

  @Autowired
  private AccountRepository accountRepository;

  /**
   * Set settings for account. Return null if no errors happened. If there
   * were errors, a message will be returned.
   *
   * @param account          account
   * @param description      description
   * @param profilePictureId profile picture id
   * @return error messaage or null
   */
  @Transactional
  public String setAccountSettings(Account account, String description, long profilePictureId) {
    // check that account is valid
    if (account == null) {
      return "invalid account";
    }
    // check description length
    if (description != null && description.length() > 200) {
      return "Description text was too long (max 200 characters)";
    }
    // if profilePictureId == -1 then set default picture
    if (profilePictureId == -1) {
      // remove boolean true from previous profilepicture
      this.resetToDefaultProfilePicture(account);
    } else if (!setProfilePicture(account, profilePictureId)) {
      // else if profilePictureId is something else, make sure that account is the
      // owner of that
      // picture and then set it as the profilepicture.
      return "Account do not own given picture.";
    }
    // set profile description
    this.setProfileDescription(account, description);
    return null;
  }

  @Transactional
  private void setProfileDescription(Account account, String description) {
    if (account != null && description.length() <= 200) {
      account.setDescription(description);
      this.accountRepository.save(account);
    }
  }

  @Transactional
  private void resetToDefaultProfilePicture(Account account) {
    if (account != null && account.getPictureAlbum() != null
        && account.getPictureAlbum().getPictures() != null) {
      for (Picture picture : account.getPictureAlbum().getPictures()) {
        if (picture.isProfilePicture()) {
          picture.setProfilePicture(false);
          this.pictureRepository.save(picture);
          break;
        }
      }
    }
  }

  /**
   * Change profilepicture to given. Returns true or false depending if the
   * operation was succesfull.
   */
  @Transactional
  private boolean setProfilePicture(Account account, long profilePictureId) {
    // check that user has pictures
    if (account == null
        || account.getPictureAlbum() == null
        || account.getPictureAlbum().getPictures() == null) {
      return false;
    }
    // loop thorugh the pictures and find previous profile pic
    // and the new profilepic
    Picture oldPic = null;
    Picture newPic = null;
    for (Picture picture : account.getPictureAlbum().getPictures()) {
      if (picture.isProfilePicture()) {
        oldPic = picture;
      }
      if (picture.getId() == profilePictureId) {
        newPic = picture;
      }
    }
    // newPic to profilepic
    if (newPic != null) {
      newPic.setProfilePicture(true);
      this.pictureRepository.save(newPic);
      // set old pic to false
      if (oldPic != null && oldPic.getId() != newPic.getId()) {
        oldPic.setProfilePicture(false);
        this.pictureRepository.save(oldPic);
      }
      return true;
    }
    return false;
  }

}
