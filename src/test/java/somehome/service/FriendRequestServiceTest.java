package somehome.service;

import static junit.framework.TestCase.assertTrue;

import java.time.LocalDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import somehome.entity.Account;
import somehome.entity.FriendRequest;
import somehome.repository.FriendRequestRepository;

/**
 * FriendRequestService unit tests.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendRequestServiceTest {

  @Autowired
  private AccountService accountService;

  @Autowired
  private FriendRequestRepository friendRequestRepository;

  @Autowired
  private FriendRequestService friendRequestService;

  @Test
  public void friendRequestFindingWorks() {
    // create accounts
    Account account1 = createTestAccount("user1", "user1p", "user1pass");
    Account account2 = createTestAccount("user2", "user2p", "user2pass");

    assertTrue(friendRequestRepository.count() == 0);

    // create friend request
    FriendRequest fr1 = new FriendRequest(account1, account2, false, LocalDateTime.now());
    friendRequestRepository.save(fr1);

    assertTrue(friendRequestService.searchNonAcceptedByReceiver(account1).isEmpty());
    assertTrue(friendRequestService.searchNonAcceptedBySender(account1).size() == 1);
    assertTrue(friendRequestService.searchNonAcceptedByReceiver(account2).size() == 1);
    assertTrue(friendRequestService.searchNonAcceptedBySender(account2).isEmpty());

    // find request bewteen accounts
    assertTrue(friendRequestService.searchRequestBetweenTwoAccounts(account1, account2) != null);
  }

  private Account createTestAccount(String username, String profilename, String password) {
    // create account object
    Account account = new Account();
    account.setUsername(username);
    account.setProfilename(profilename);
    account.setPassword(password);

    // create account with service
    accountService.createAccount(account);
    return accountService.findByProfilename(profilename);
  }
}
