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
public class PictureAlbumControllerTest extends org.fluentlenium.adapter.junit.FluentTest {

    @LocalServerPort
    private Integer port;
    
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void userCanViewOwnPictureAlbum() {
        // 1. Create user
        createUser("user1", "user1prof", "user1pass");

        // 2. login
        loginAsUser("user1", "user1pass");

        // 3. go to picture album page
        goTo("http://localhost:" + port + "/picture_album");

        // 4. check that page is correct
        assertTrue(window().title().equals("Picture Album"));
    }
    
    @Test
    public void userCanViewOtherUsersPictureAlbum() {
        // 1. Create user
        createUser("user1", "user1prof", "user1pass");
        createUser("user2", "user2prof", "user2pass");

        // 2. login
        loginAsUser("user1", "user1pass");

        // 3. go to other users picture album
        long id = accountRepository.findByUsername("user2").getPictureAlbum().getId();
        goTo("http://localhost:" + port + "/picture_album/" + id);
        
        // 4. check that page is correct
        assertTrue(window().title().equals("Picture Album")); 
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
