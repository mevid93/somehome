package somehome.service;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * AuthenticationService unit tests.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationServiceTest {

  @Autowired
  private AuthenticationService authenticationService;

  @Test
  public void isAuthenticatedWorksForUnauthenticatedUser() {
    assertFalse(authenticationService.isAuthenticated());
  }

  @Test
  public void getUsernameWorksForUnauthenticatedUser() {
    assertTrue(authenticationService.getUsername().isEmpty());
  }

}
