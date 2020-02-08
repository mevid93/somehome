package projekti.service;

import static junit.framework.TestCase.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projekti.entity.Account;
import projekti.repository.CommentRepository;
import projekti.repository.MessageRepository;
import projekti.repository.MyLikeRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MyLikeRepository myLikeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void createMessageWorks() {
        // create account
        Account account = createTestAccount("account1", "account1prof", "account1pass");

        // make sure that is empty
        assertTrue(messageRepository.count() == 0);

        // create message
        messageService.createMessage(account, account, "hello message");

        // check that was created
        assertTrue(messageRepository.count() == 1);

    }

    @Test
    public void createLikeForMessageWorks() {
        // create account
        Account account = createTestAccount("account1", "account1prof", "account1pass");

        // make sure that is empty
        assertTrue(messageRepository.count() == 0);
        assertTrue(myLikeRepository.count() == 0);

        // create message
        messageService.createMessage(account, account, "hello message");
        assertTrue(messageRepository.findAll().get(0).getLikeCounter() == 0);

        // create like for message
        messageService.createLike(account, messageRepository.findAll().get(0));

        // make sure that likes increased and new like was added to repository
        assertTrue(myLikeRepository.count() == 1);
        assertTrue(messageRepository.findAll().get(0).getLikeCounter() == 1);
    }

    @Test
    public void userCanLikeMessageOnlyOnce() {
        // create account
        Account account = createTestAccount("account1", "account1prof", "account1pass");

        // make sure that is empty
        assertTrue(messageRepository.count() == 0);
        assertTrue(myLikeRepository.count() == 0);

        // create message
        messageService.createMessage(account, account, "hello message");
        assertTrue(messageRepository.findAll().get(0).getLikeCounter() == 0);

        // create like for message
        messageService.createLike(account, messageRepository.findAll().get(0));

        // make sure that likes increased and new like was added to repository
        assertTrue(myLikeRepository.count() == 1);
        assertTrue(messageRepository.findAll().get(0).getLikeCounter() == 1);
        
        // create like for message
        messageService.createLike(account, messageRepository.findAll().get(0));

        // make sure that likes increased and new like was added to repository
        assertTrue(myLikeRepository.count() == 1);
        assertTrue(messageRepository.findAll().get(0).getLikeCounter() == 1);
    }

    @Test
    public void createCommentForMessageWorks() {
        // create account
        Account account = createTestAccount("account1", "account1prof", "account1pass");

        // make sure that is empty
        assertTrue(commentRepository.count() == 0);

        // create message
        messageService.createMessage(account, account, "hello message");

        // create comment for message
        messageService.createComment(account, messageRepository.findAll().get(0), "My comment");

        // make sure that new comment was added to repository
        assertTrue(commentRepository.count() == 1);
    }

    private Account createTestAccount(String username, String profilename, String password) {
        // create account object
        Account account = new Account();
        account.setUsername(username);
        account.setProfilename(profilename);
        account.setPassword(password);

        // create account with service
        accountService.createAccount(account);
        return accountService.findByProfilename(profilename);
    }

}
