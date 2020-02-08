package projekti.service;

import java.time.LocalDateTime;
import java.util.List;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projekti.entity.Account;
import projekti.entity.FriendRequest;
import projekti.repository.FriendRequestRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendService friendService;

    @Test
    public void findFriendsForAccountWorks() {
        // create accounts
        Account account1 = createTestAccount("user1", "user1p", "user1pass");
        Account account2 = createTestAccount("user2", "user2p", "user2pass");
        Account account3 = createTestAccount("user3", "user3p", "user3pass");

        // create friend request
        FriendRequest fr1 = new FriendRequest(account1, account2, false, LocalDateTime.now());
        FriendRequest fr2 = new FriendRequest(account1, account3, true, LocalDateTime.now());
        friendRequestRepository.save(fr1);
        friendRequestRepository.save(fr2);

        // find friends
        List<Account> friends = friendService.findFriendsForAccount(account1);

        assertTrue(friends.size() == 1);

        assertTrue(friends.get(0).getProfilename().equals("user3p"));
    }

    @Test
    public void areAccountsFriendsWorks() {
        // create accounts
        Account account1 = createTestAccount("user1", "user1p", "user1pass");
        Account account2 = createTestAccount("user2", "user2p", "user2pass");
        Account account3 = createTestAccount("user3", "user3p", "user3pass");
        Account account4 = createTestAccount("user4", "user4p", "user4pass");

        // create friend request
        FriendRequest fr1 = new FriendRequest(account1, account2, false, LocalDateTime.now());
        FriendRequest fr2 = new FriendRequest(account1, account3, true, LocalDateTime.now());
        friendRequestRepository.save(fr1);
        friendRequestRepository.save(fr2);

        assertTrue(friendService.areAccountsFriends(account1, account2) == 1);
        assertTrue(friendService.areAccountsFriends(account1, account3) == 2);
        assertTrue(friendService.areAccountsFriends(account1, account4) == 0);
    }
    
    @Test
    public void deleteFriendShipWorks() {
        // create accounts
        Account account1 = createTestAccount("user1", "user1p", "user1pass");
        Account account2 = createTestAccount("user2", "user2p", "user2pass");

        // create friend request
        FriendRequest fr1 = new FriendRequest(account1, account2, true, LocalDateTime.now());
        friendRequestRepository.save(fr1);

        friendService.deleteFriendship(account1, account2);
        
        assertTrue(friendRequestRepository.count() == 0);
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
