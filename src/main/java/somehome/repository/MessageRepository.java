package somehome.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somehome.entity.Account;
import somehome.entity.Message;

/**
 * Message repository class.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  @EntityGraph(attributePaths = { "sender", "comments" })
  List<Message> findByReceiver(Account receiver, Pageable pageable);

}
