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
import projekti.repository.AccountRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterControllerTest extends org.fluentlenium.adapter.junit.FluentTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void unauthenticatedUserCanAccessRegisterPage() {
        // 1. go to register page when user is unauthenticated
        goTo("http://localhost:" + port + "/register");

        // 2. make sure that current page is register page
        assertTrue(window().title().contains("Register"));
    }

    @Test
    public void unauthenticatedUserCanCreateAccount() {
        // 1. make sure that account repository is empty
        assertTrue(accountRepository.count() == 0);

        // 2. go to register page when user is unauthenticated
        goTo("http://localhost:" + port + "/register");

        // 3. fill user information
        find("#username").fill().with("admin93");
        find("#profilename").fill().with("admin93");
        find("#password").fill().with("admin1993");

        // 4. send form --> redirect to login-page
        find("form").first().submit();

        // 5. make sure that account repository is not empty
        assertTrue(accountRepository.count() == 1);
    }

    @Test
    public void authenticatedUserCanNotAccessRegisterPage() {
        // 1. go to register page
        goTo("http://localhost:" + port + "/register");

        // 2. fill user information
        find("#username").fill().with("admin93");
        find("#profilename").fill().with("admin93");
        find("#password").fill().with("admin1993");

        // 3. send form --> redirect to login-page
        find("form").first().submit();

        // 4. fill login information and send --> redirect to home page
        find("#username").fill().with("admin93");
        find("#password").fill().with("admin1993");
        find("form").first().submit();

        // 5. go to register page
        goTo("http://localhost:" + port + "/register");

        // 6. make sure that user has been redirected to home page
        assertTrue(window().title().contains("Home"));
    }

    @Test
    public void userCanNotRegisterAccountWhichUsernameHasBeenTaken() {
        // 1. go to register page
        goTo("http://localhost:" + port + "/register");

        // 2. fill user information
        find("#username").fill().with("admin93");
        find("#profilename").fill().with("admin93");
        find("#password").fill().with("admin1993");

        // 3. send form --> redirect to login-page
        find("form").first().submit();

        // 4. go to register page
        goTo("http://localhost:" + port + "/register");

        // 5. fill user information
        find("#username").fill().with("admin93");
        find("#profilename").fill().with("admin39");
        find("#password").fill().with("admin1993");

        // 6. send form --> redirect to register page because username was taken
        find("form").first().submit();

        // 7. make sure that user is at register-page
        assertTrue(window().title().contains("Register"));
    }

    @Test
    public void userCanNotRegisterAccountWhichProfilenameHasBeenTaken() {
        // 1. go to register page
        goTo("http://localhost:" + port + "/register");

        // 2. fill user information
        find("#username").fill().with("admin93");
        find("#profilename").fill().with("admin93");
        find("#password").fill().with("admin1993");

        // 3. send form --> redirect to login-page
        find("form").first().submit();

        // 4. go to register page
        goTo("http://localhost:" + port + "/register");

        // 5. fill user information
        find("#username").fill().with("admin39");
        find("#profilename").fill().with("admin93");
        find("#password").fill().with("admin1993");

        // 6. send form --> redirect to register page because username was taken
        find("form").first().submit();

        // 7. make sure that user is at register-page
        assertTrue(window().title().contains("Register"));
    }

}
