package projekti.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entity.Account;
import projekti.entity.PictureAlbum;

public interface PictureAlbumRepository extends JpaRepository<PictureAlbum, Long> {

    @EntityGraph(attributePaths = {"pictures"})
    PictureAlbum findByOwner(Account owner);
    
    @EntityGraph(attributePaths = {"owner", "pictures"})
    PictureAlbum findById(long id);
}
