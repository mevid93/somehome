package projekti.controller;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projekti.repository.PictureRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SettingsControllerTest extends org.fluentlenium.adapter.junit.FluentTest {

    @LocalServerPort
    private Integer port;

    @Test
    public void userCanAccessSettingsPage() {
        // 1. Create user
        createUser("user1", "user1prof", "user1pass");

        // 2. login
        loginAsUser("user1", "user1pass");

        // 3. go to picture album page
        goTo("http://localhost:" + port + "/settings");

        // 4. make sure that current page is settings page
        assertTrue(window().title().contains("Settings"));
    }

    @Test
    public void userCanChangeAboutMeText() {
        // 1. Create user
        createUser("user1", "user1prof", "user1pass");

        // 2. login
        loginAsUser("user1", "user1pass");

        // 3. make sure that page does not contain "My Cool Description" -text
        assertFalse(pageSource().contains("My Cool Description"));

        // 4. go to settings page
        goTo("http://localhost:" + port + "/settings");
        
        // 5. change description text
        find("#description").fill().with("My Cool Description");
        
        // 6. submit form
        find("#profile_settings_form").submit();
        
        // 7. make sure that current page is home
        assertTrue(window().title().contains("Home"));
        
        // 8. make sure that current page contains new profile description aka about me text
        assertTrue(pageSource().contains("My Cool Description"));
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
