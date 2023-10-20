package somehome.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somehome.entity.Account;

/**
 * Account repository class.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  @EntityGraph(attributePaths = { "pictureAlbum" })
  Account findByUsername(String username);

  Account findByProfilename(String profilename);

  List<Account> findByProfilenameContainingIgnoreCase(String profilename, Pageable pageable);
}
