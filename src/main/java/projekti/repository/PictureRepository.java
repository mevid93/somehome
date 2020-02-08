package projekti.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projekti.entity.Account;
import projekti.entity.Picture;
import projekti.entity.PictureAlbum;

public interface PictureRepository extends JpaRepository<Picture, Long> {

    List<Picture> findByPictureAlbum(PictureAlbum pictureAlbum);
    
    @Query("SELECT p FROM Picture p, PictureAlbum a WHERE a.owner =:account AND p.pictureAlbum = a AND p.profilePicture = TRUE")
    Picture getAccountProfilePicture(@Param("account") Account account);
    
    @Query("SELECT a FROM Account a, Picture p, PictureAlbum pa WHERE p =:picture AND p.pictureAlbum = pa AND pa = a.pictureAlbum")
    Account getPictureOwner(@Param("picture") Picture picture);
    
    @Query("SELECT p.content FROM Picture p WHERE p.id = :pictureId")
    byte[] getContentForPicture(@Param("pictureId") long pictureId);
    
    Picture getById(long id);
    
}
