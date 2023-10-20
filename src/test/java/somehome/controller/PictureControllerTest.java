package somehome.controller;

import static junit.framework.TestCase.assertTrue;

import io.fluentlenium.adapter.junit.FluentTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import somehome.repository.PictureRepository;

/**
 * PictureController unit tests.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PictureControllerTest extends FluentTest {

  @LocalServerPort
  private Integer port;

  @Autowired
  private PictureRepository pictureRepository;

  @Test
  public void userCanViewPictureManagementPage() {
    // 1. Create user
    createUser("user1", "user1prof", "user1pass");

    // 2. login
    loginAsUser("user1", "user1pass");

    // 3. go to picture album page
    goTo("http://localhost:" + port + "/picture_album");

    // 4. click link "Manage Pictures
    find(By.linkText("Manage Pictures")).click();

    // 5. make sure that current page is picture management page
    assertTrue(window().title().contains("Pictures"));
  }

  @Test
  public void userCantAddEmptyPictureToDatabase() {
    // 1. Create user
    createUser("user1", "user1prof", "user1pass");

    // 2. login
    loginAsUser("user1", "user1pass");

    // 3. go to picture album page
    goTo("http://localhost:" + port + "/picture_album");

    // 4. click link "Manage Pictures
    find(By.linkText("Manage Pictures")).click();

    // 5. make sure that current page is picture management page
    assertTrue(window().title().contains("Pictures"));

    // 6. try to add empty picture to db
    find("#picture_add_form").submit();

    // 7. make sure that current page is still picture management page
    assertTrue(window().title().contains("Pictures"));

    // 8. check that no pictures were added
    assertTrue(pictureRepository.count() == 0);
  }

  private void createUser(String username, String profilename, String password) {
    // 0. go to user page
    goTo("http://localhost:" + port + "/register");

    // 1. fill user information
    find("#username").fill().with(username);
    find("#profilename").fill().with(profilename);
    find("#password").fill().with(password);

    // 2. send form --> redirect to login-page
    $("#register_form").submit();

  }

  private void loginAsUser(String username, String password) {
    // 0. go to user page
    goTo("http://localhost:" + port + "/login");

    // 1. fill login information and send --> redirect to home page
    find("#username").fill().with(username);
    find("#password").fill().with(password);
    $("#login_form").submit();
  }
}
