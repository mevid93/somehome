package projekti.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekti.entity.Account;
import projekti.entity.PictureAlbum;
import projekti.repository.PictureAlbumRepository;

@Service
public class PictureAlbumService {

    @Autowired
    private PictureAlbumRepository pictureAlbumRepository;

    /**
     * Return Picture album corresponding the account given as parameter.
     *
     * @param account
     * @return
     */
    @Transactional
    public PictureAlbum getPictureAlbumForAccount(Account account) {
        if (account == null) {
            return null;
        }
        return pictureAlbumRepository.findByOwner(account);
    }

    /**
     * Create picture album for those who do not have one.
     *
     * @param account
     */
    @Transactional
    public void createPictureAlbumIfNotExist(Account account) {
        // if account does not exist or account has picturealbum alredy
        if (account == null || account.getPictureAlbum() != null) {
            return;
        }
        PictureAlbum album = new PictureAlbum();
        album.setOwner(account);
        account.setPictureAlbum(album);
        pictureAlbumRepository.save(album);
    }

    /**
     * Get picture album info.
     *
     * @param id
     * @return
     */
    @Transactional
    public PictureAlbum getPictureAlbumById(long id) {
        return pictureAlbumRepository.findById(id);
    }

}
