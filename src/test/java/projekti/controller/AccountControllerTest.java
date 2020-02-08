package projekti.controller;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerTest extends org.fluentlenium.adapter.junit.FluentTest {

    @LocalServerPort
    private Integer port;

    @Test
    public void authenticatedUserCanAccessHomePage() {
        // 1. create user
        createUser("user1", "user1profile", "user1pass");

        // 2. login as user
        loginAsUser("user1", "user1pass");

        // 3. go to home page
        goTo("http://localhost:" + port + "/home");

        // 4. make sure that current page is home page
        assertTrue(window().title().equals("Home"));
    }

    @Test
    public void searchIsLimitedToTenUsers() {
        // 1. create enough users
        createXAmountUsers(15);

        // 2. login as first user
        loginAsUser("username1", "password1");

        // 3. search without searchword --> 10 random users
        find("#user_search_form").submit();

        // 4. make sure that page has listed 10 users
        String source = pageSource().replaceAll("\\s+", "");
        assertTrue(source.contains("<p>10</p>"));
        assertFalse(source.contains("<p>11</p>"));

    }

    @Test
    public void searchReturnsMatchedAccounAndUserCanProceedTo() {
        // 1. create three users
        createXAmountUsers(3);

        // 2. login as first user
        loginAsUser("username1", "password1");

        // 3. search second user
        find("#usersearchfield").fill().with("profilename2");
        find("#user_search_form").submit();

        // 4. make sure that page contains username2 but not username 3
        String source = pageSource().replaceAll("\\s+", "");
        assertTrue(source.contains("profilename2"));
        assertFalse(source.contains("profilename3"));
        
        // 5. click username2
        find(By.linkText("profilename2")).click();
        
        // 6. make sure that current page is usersname2's page
        assertTrue(window().title().equals("Account"));
        source = pageSource().replaceAll("\\s+", "");
        assertTrue(source.contains("profilename2"));
        
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

    private void createXAmountUsers(int x) {
        for (int i = 1; i <= x; i++) {
            String username = "username" + i;
            String profilename = "profilename" + i;
            String password = "password" + i;
            createUser(username, profilename, password);
        }
    }
}
