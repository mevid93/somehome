package somehome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somehome.entity.Account;
import somehome.entity.Message;
import somehome.entity.MyLike;
import somehome.entity.Picture;

/**
 * MyLike repository class.
 */
@Repository
public interface MyLikeRepository extends JpaRepository<MyLike, Long> {

  MyLike findByAccountAndMessage(Account account, Message message);

  MyLike findByAccountAndPicture(Account account, Picture picture);

}
