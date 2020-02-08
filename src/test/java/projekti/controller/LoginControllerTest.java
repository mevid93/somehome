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
public class LoginControllerTest extends org.fluentlenium.adapter.junit.FluentTest {

    @LocalServerPort
    private Integer port;

    @Test
    public void unauthenticatedUserCanAccessLoginPage() {
        // 1. go to login page when user is unauthenticated
        goTo("http://localhost:" + port + "/login");

        // 2. make sure that current page is login page
        assertTrue(window().title().contains("Login"));
    }

    @Test
    public void unauthenticatedUserCanLogToAccount() {
        // 1. register a user
        register("admin93", "admin93", "admin1993"); 

        // 2. fill login information and send --> redirect to home page
        find("#username").fill().with("admin93");
        find("#password").fill().with("admin1993");
        find("form").first().submit();

        // 5. make sure that user has been redirected to home page
        assertTrue(window().title().contains("Home"));
    }

    @Test
    public void authenticatedUserCanNotAccessLoginPage() {
        // 1. register a user
        register("admin93", "admin93", "admin1993");

        // 2. fill login information and send --> redirect to home page
        find("#username").fill().with("admin93");
        find("#password").fill().with("admin1993");
        find("form").first().submit();

        // 3. go to login page
        goTo("http://localhost:" + port + "/login");

        // 4. make sure that user has been redirected to home page
        assertTrue(window().title().contains("Home"));
    }

    @Test
    public void userCanNotLogInWithInvalidCredentials() {
        // 1. go to login page
        goTo("http://localhost:" + port + "/login");

        // 2. fill invalid login information and send --> redirect to login page
        find("#username").fill().with("admin93");
        find("#password").fill().with("admin1993");
        find("form").first().submit();

        // 3. make sure that user has been redirected to login page
        assertTrue(window().title().contains("Login"));

        // 4. make sure that page contains error message
        assertTrue(pageSource().contains("Incorrect Username or Password!"));
    }

    @Test
    public void userCanLogOut() {
        // 1. register a user
        register("admin93", "admin93", "admin1993");

        // 2. fill login information and send --> redirect to home page
        find("#username").fill().with("admin93");
        find("#password").fill().with("admin1993");
        find("form").first().submit();

        // 3. click logout button
        find("#logoutform").submit();

        // 4. make sure that user has been redirected to login page
        assertTrue(window().title().contains("Login"));
    }

    private void register(String username, String profilename, String password) {
        // 1. go to register page
        goTo("http://localhost:" + port + "/register");

        // 2. fill user information
        find("#username").fill().with(username);
        find("#profilename").fill().with(profilename);
        find("#password").fill().with(password);

        // 3. send form --> redirect to login-page
        find("form").first().submit();
    }
}
