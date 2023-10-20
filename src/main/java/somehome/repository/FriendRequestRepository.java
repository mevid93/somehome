package somehome.repository;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import somehome.entity.Account;
import somehome.entity.FriendRequest;

/**
 * Friend request repository class.
 */
@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

  @EntityGraph(attributePaths = { "sender", "receiver" })
  List<FriendRequest> findBySenderAndAccepted(Account sender, boolean accepted);

  @EntityGraph(attributePaths = { "sender", "receiver" })
  List<FriendRequest> findByReceiverAndAccepted(Account receiver, boolean accepted);

  FriendRequest findBySenderAndReceiver(Account sender, Account receiver);

  // NO UNION SUPPORT IN JPA --> MUST USE SUBQUERIES

  @Query("SELECT a FROM Account a "
      + "WHERE a IN (SELECT r.sender FROM FriendRequest r "
      + "WHERE r.receiver =:account AND r.accepted = TRUE) "
      + "OR a IN (SELECT r.receiver FROM FriendRequest r "
      + "WHERE r.sender =:account AND r.accepted = TRUE) "
      + "ORDER BY a.profilename ASC")
  List<Account> findFriendsForAccount(@Param("account") Account account);

}
