package somehome.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import somehome.entity.Account;
import somehome.entity.PictureAlbum;
import somehome.repository.PictureAlbumRepository;

/**
 * Picture album service class.
 */
@Service
public class PictureAlbumService {

  @Autowired
  private PictureAlbumRepository pictureAlbumRepository;

  /**
   * Return Picture album corresponding the account given as parameter.
   *
   * @param account account
   * @return picture album
   */
  @Transactional
  public PictureAlbum getPictureAlbumForAccount(Account account) {
    if (account == null) {
      return null;
    }

    PictureAlbum pictureAlbum = pictureAlbumRepository.findByOwner(account);
    if (pictureAlbum == null) {
      pictureAlbum = createPictureAlbumIfNotExist(account);
    }

    return pictureAlbum;
  }

  /**
   * Create picture album for those who do not have one.
   *
   * @param account account
   */
  @Transactional
  public PictureAlbum createPictureAlbumIfNotExist(Account account) {
    // if account does not exist or account has picturealbum alredy
    if (account == null || account.getPictureAlbum() != null) {
      return null;
    }
    PictureAlbum album = new PictureAlbum();
    album.setOwner(account);
    account.setPictureAlbum(album);
    return pictureAlbumRepository.save(album);
  }

  /**
   * Get picture album info.
   *
   * @param id id
   * @return picture album
   */
  @Transactional
  public PictureAlbum getPictureAlbumById(long id) {
    return pictureAlbumRepository.findById(id);
  }

}
