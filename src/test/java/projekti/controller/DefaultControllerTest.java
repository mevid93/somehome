package projekti.controller;

import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DefaultControllerTest extends org.fluentlenium.adapter.junit.FluentTest {

    @LocalServerPort
    private Integer port;

    @Test
    public void unauthenticatedUserIsRedirectedToLoginPage() {
        // 1. go to home page when user is unauthenticated
        goTo("http://localhost:" + port + "/home");

        // 2. make sure that current page is login page
        assertTrue(window().title().contains("Login"));
    }

    @Test
    public void authenticatedUserIsRedirectedToHomePage() {
        // 1. go to register page
        goTo("http://localhost:" + port + "/register");

        // 2. register
        createUser("admin93", "admin93", "admin1993");

        // 3. login
        loginAsUser("admin93", "admin1993");

        // 5. try to go some random address
        goTo("http://localhost:" + port + "/addressThatDoesNotExist");

        // 6. make sure that user has been redirected to home page
        assertTrue(window().title().contains("Home"));
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
