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
import projekti.repository.MessageRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageControllerTest extends org.fluentlenium.adapter.junit.FluentTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Test
    public void accountCanWriteMessageToHisWall() {
        // 1. create user and login
        createUser("admin93", "admin93", "admin1993");
        loginAsUser("admin93", "admin1993");
        
        // 2. make sure that messageRepository is empty
        assertTrue(messageRepository.count() == 0);

        // 3. send message to users wall
        find("#message_input").fill().with("Cool message");
        find("#message_add_form").submit();

        // 4. check that messageRepository has message
        assertTrue(messageRepository.count() == 1);

        // 5. make sure that current page is home
        assertTrue(window().title().contains("Home"));

        // 6. make sure that users wall has message
        assertTrue(pageSource().contains("Cool message"));
    }

    @Test
    public void accountCantAddEmptyMessage() {
        // 1. create user and login
        createUser("admin93", "admin93", "admin1993");
        loginAsUser("admin93", "admin1993");
        
        // 2. make sure that messageRepository is empty
        assertTrue(messageRepository.count() == 0);

        // 3. send message to users wall
        find("#message_add_form").submit();

        // 4. check that messageRepository has no message
        assertTrue(messageRepository.count() == 0);

        // 5. make sure that current page is home
        assertTrue(window().title().contains("Home"));
    }

    @Test
    public void accountCanAddMessageToHisFriendsPage() {
        // 1. create users
        createUser("user1", "user1prof", "user1pass");
        createUser("user2", "user2prof", "user2pass");

        // 2. login as first user
        loginAsUser("user1", "user1pass");

        // 3. go to second users page
        goTo("http://localhost:" + port + "/accounts/user2prof");

        // 4. send friend request
        find("#friend_request_form").submit();

        // 5. log out from user1
        find("#logoutform").submit();

        // 6. login as user2
        loginAsUser("user2", "user2pass");

        // 7. go to friend_requests page
        goTo("http://localhost:" + port + "/friend_requests");

        // 8. accept friend request
        find("#request_accept_form" + friendRequestRepository.findAll().get(0).getId()).submit();

        // 9. goto friend's page
        goTo("http://localhost:" + port + "/accounts/user1prof");

        // 10. send message to users wall
        find("#message_input").fill().with("Cool message");
        find("#message_add_form").submit();

        // 11. check that messageRepository has message
        assertTrue(messageRepository.count() == 1);

        // 12. make sure that current page is home
        assertTrue(window().title().contains("Account"));

        // 13. make sure that users wall has message
        assertTrue(pageSource().contains("Cool message"));
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
