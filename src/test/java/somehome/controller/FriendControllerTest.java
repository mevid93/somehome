package somehome.controller;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import io.fluentlenium.adapter.junit.FluentTest;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import somehome.repository.AccountRepository;
import somehome.repository.FriendRequestRepository;

/**
 * FriendController unit tests.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendControllerTest extends FluentTest {

  @LocalServerPort
  private Integer port;

  @Autowired
  private FriendRequestRepository friendRequestRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Test
  public void userCanViewFriends() {
    // 1. create two users
    createUser("user1", "user1", "user1pass");
    createUser("user2", "user2", "user2pass");

    // 2. make sure that no friend requests exists
    assertTrue(friendRequestRepository.count() == 0);
    assertEquals("Account creation failed", 2, accountRepository.count(), 0);

    // 3. login as first user
    loginAsUser("user1", "user1pass");

    // 4. go to second user's page
    goTo("http://localhost:" + port + "/accounts/user2");

    await().atMost(5, TimeUnit.SECONDS).until($("#friend_request_form")).present();

    // 5. send friend request
    find("#friend_request_form").submit();

    // 6. make sure that request was added to database
    assertTrue(friendRequestRepository.count() == 1);

    // 7. log out from user1
    find("#logoutform").submit();

    // 8. login as user2
    loginAsUser("user2", "user2pass");

    // 9. go to friend_requests page
    goTo("http://localhost:" + port + "/friend_requests");

    // 10. accept friend request
    find("#request_accept_form" + friendRequestRepository.findAll().get(0).getId()).submit();

    // 11. make sure that request was accepted
    assertTrue(friendRequestRepository.findAll().get(0).isAccepted());

    // 12. goto friends page
    goTo("http://localhost:" + port + "/friends");

    // 13. make sure that page source contains user1 profilename
    assertTrue(pageSource().contains("user1"));
  }

  @Test
  public void userCanAccessFriendPageFromFriendsList() {
    // 1. create two users
    createUser("user1", "user1", "user1pass");
    createUser("user2", "user2", "user2pass");

    // 2. login as first user
    loginAsUser("user1", "user1pass");

    // 3. go to second user's page
    goTo("http://localhost:" + port + "/accounts/user2");

    await().atMost(5, TimeUnit.SECONDS).until($("#friend_request_form")).present();

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

    // 9. goto friends page
    goTo("http://localhost:" + port + "/friends");

    // 10. make sure that page source contains user1 profilename
    assertTrue(pageSource().contains("user1"));

    // 11. click link that takes to frind's page
    find(By.linkText("user1")).click();

    // 12. make sure that current page is friends page
    assertTrue(window().title().equals("Account"));
  }

  @Test
  public void userCanDeleteFriend() {
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

    // 7. log out from user1
    find("#logoutform").submit();

    // 8. login as user2
    loginAsUser("user2", "user2pass");

    // 9. go to friend_requests page
    goTo("http://localhost:" + port + "/friend_requests");

    // 10. accept friend request
    find("#request_accept_form" + friendRequestRepository.findAll().get(0).getId()).submit();

    // 11. make sure that request was accepted
    assertTrue(friendRequestRepository.findAll().get(0).isAccepted());

    // 12. goto friends page
    goTo("http://localhost:" + port + "/friends");

    // 13. make sure that page source contains user1 profilename
    assertTrue(pageSource().contains("user1"));

    // 14. delete user from friends
    long id = accountRepository.findByProfilename("user1").getId();
    find("#friend_delete_form" + id).submit();

    // 15. make sure that friendRequestRepository is empty
    assertTrue(friendRequestRepository.count() == 0);
  }

  private void createUser(String username, String profilename, String password) {
    // 0. go to user page
    goTo("http://localhost:" + port + "/register");

    await().atMost(5, TimeUnit.SECONDS).until($("#profilename")).present();

    // 1. fill user information
    $("#username").clear();
    $("#profilename").clear();
    $("#password").clear();

    $("#username").fill().with(username);
    $("#profilename").fill().with(profilename);
    $("#password").fill().with(password);

    // 2. send form --> redirect to login-page
    find("#register_form").submit();

  }

  private void loginAsUser(String username, String password) {
    // 0. go to user page
    goTo("http://localhost:" + port + "/login");

    await().atMost(5, TimeUnit.SECONDS).until($("#username")).present();

    // 1. fill login information and send --> redirect to home page
    
    $("#username").clear();
    $("#password").clear();

    $("#username").fill().with(username);
    $("#password").fill().with(password);

    $("#login_form").submit();
  }

}
