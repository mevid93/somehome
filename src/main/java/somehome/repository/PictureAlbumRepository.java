package somehome.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import somehome.entity.Account;
import somehome.entity.PictureAlbum;

/**
 * Picture album repository class.
 */
@Repository
public interface PictureAlbumRepository extends JpaRepository<PictureAlbum, Long> {

  @EntityGraph(attributePaths = { "pictures" })
  @Query("SELECT a FROM PictureAlbum a "
      + "WHERE a.owner =:account")
  PictureAlbum findByOwner(@Param("account") Account owner);

  @EntityGraph(attributePaths = { "owner", "pictures" })
  PictureAlbum findById(long id);
}
