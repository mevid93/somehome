package projekti.controller;

import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projekti.repository.FriendRequestRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendRequestControllerTest extends org.fluentlenium.adapter.junit.FluentTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Test
    public void userCanSendFriendRequest() {
        // 1. create two users
        createUser("user1", "user1", "user1pass");
        createUser("user2", "user2", "user2pass");

        // 2. make sure that no friend requests exists
        assertTrue(friendRequestRepository.count() == 0);

        // 3. login as first user
        loginAsUser("user1", "user1pass");

        // 4. go to second user's page
        goTo("http://localhost:" + port + "/accounts/user2");

        // 5. send friend request
        find("#friend_request_form").submit();

        // 6. make sure that request was added to database
        assertTrue(friendRequestRepository.count() == 1);

        // 7. make sure that current page is friend_requests
        assertTrue(window().title().contains("Friend Requests"));

    }

    @Test
    public void userCanCancelSentMessage() {
        // 1. create two users
        createUser("user1", "user1", "user1pass");
        createUser("user2", "user2", "user2pass");

        // 2. make sure that no friend requests exists
        assertTrue(friendRequestRepository.count() == 0);

        // 3. login as first user
        loginAsUser("user1", "user1pass");

        // 4. go to second user's page
        goTo("http://localhost:" + port + "/accounts/user2");

        // 5. send friend request
        find("#friend_request_form").submit();

        // 6. make sure that request was added to database
        assertTrue(friendRequestRepository.count() == 1);

        // 7. make sure that current page is friend_requests
        assertTrue(window().title().contains("Friend Requests"));

        // 8. cancel friend request
        find("#request_cancel_form" + friendRequestRepository.findAll().get(0).getId()).submit();

        // 9. make sure that request was deleted
        assertTrue(friendRequestRepository.count() == 0);
    }

    @Test
    public void userCanAcceptReceivedMessage() {
        // 1. create two users
        createUser("user1", "user1", "user1pass");
        createUser("user2", "user2", "user2pass");

        // 2. make sure that no friend requests exists
        assertTrue(friendRequestRepository.count() == 0);

        // 3. login as first user
        loginAsUser("user1", "user1pass");

        // 4. go to second user's page
        goTo("http://localhost:" + port + "/accounts/user2");

        // 5. send friend request
        find("#friend_request_form").submit();

        // 6. make sure that request was added to database
        assertTrue(friendRequestRepository.count() == 1);

        // 8. logout as first user
        find("#logoutform").submit();

        // 9. login as second user
        loginAsUser("user2", "user2pass");

        // 10. go to friend_requests page
        goTo("http://localhost:" + port + "/friend_requests");

        // 11. accept friend request
        find("#request_accept_form" + friendRequestRepository.findAll().get(0).getId()).submit();

        // 12. make sure that request was accepted
        assertTrue(friendRequestRepository.findAll().get(0).isAccepted());
    }

    @Test
    public void userCanDeclineReceivedMessage() {
        // 1. create two users
        createUser("user1", "user1", "user1pass");
        createUser("user2", "user2", "user2pass");

        // 2. make sure that no friend requests exists
        assertTrue(friendRequestRepository.count() == 0);

        // 3. login as first user
        loginAsUser("user1", "user1pass");

        // 4. go to second user's page
        goTo("http://localhost:" + port + "/accounts/user2");

        // 5. send friend request
        find("#friend_request_form").submit();

        // 6. make sure that request was added to database
        assertTrue(friendRequestRepository.count() == 1);

        // 8. logout as first user
        find("#logoutform").submit();

        // 9. login as second user
        loginAsUser("user2", "user2pass");

        // 10. go to friend_requests page
        goTo("http://localhost:" + port + "/friend_requests");

        // 11. accept friend request
        find("#request_decline_form" + friendRequestRepository.findAll().get(0).getId()).submit();

        // 12. make sure that request was accepted
        assertTrue(friendRequestRepository.count() == 0);
    }

    private void createUser(String username, String profilename, String password) {
        // 0. go to user page
        goTo("http://localhost:" + port + "/register");

        // 1. fill user information
        find("#username").fill().with(username);
        find("#profilename").fill().with(profilename);
        find("#password").fill().with(password);

        // 2. send form --> redirect to login-page
        find("form").first().submit();

    }

    private void loginAsUser(String username, String password) {
        // 0. go to user page
        goTo("http://localhost:" + port + "/login");

        // 1. fill login information and send --> redirect to home page
        find("#username").fill().with(username);
        find("#password").fill().with(password);
        find("form").first().submit();
    }

}
