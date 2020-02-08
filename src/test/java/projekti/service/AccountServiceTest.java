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
import projekti.repository.AccountRepository;
import projekti.repository.PictureAlbumRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PictureAlbumRepository pictureAlbumRepository;

    @Test
    public void creatingAccountWorks() {
        // create account object
        Account account = new Account();
        account.setUsername("username");
        account.setProfilename("profilename");
        account.setPassword("password666");
        account.setDescription("Jee jee");

        // check that account and picture repositories are empty
        assertTrue(accountRepository.count() == 0);
        assertTrue(pictureAlbumRepository.count() == 0);

        // create account with service
        accountService.createAccount(account);

        // check that account and it's picture album was created
        assertTrue(accountRepository.count() == 1);
        assertTrue(pictureAlbumRepository.count() == 1);
    }

    @Test
    public void findAccountByProfilenameWorks() {
        // create two accounts
        Account account = new Account();
        account.setUsername("username");
        account.setProfilename("profilename");
        account.setPassword("password666");
        account.setDescription("Jee jee");

        // create two accounts
        Account account1 = new Account();
        account1.setUsername("username2");
        account1.setProfilename("profilename2");
        account1.setPassword("password666121");
        account1.setDescription("Jee jee");

        // save to repository
        accountService.createAccount(account);
        accountService.createAccount(account1);

        // find account by profilename
        Account found = accountService.findByProfilename("profilename2");
        assertTrue(found.getProfilename().equals("profilename2"));
    }

    @Test
    public void findAccountByUsernameWorks() {
        // create two accounts
        Account account = new Account();
        account.setUsername("username");
        account.setProfilename("profilename");
        account.setPassword("password666");
        account.setDescription("Jee jee");

        // create two accounts
        Account account1 = new Account();
        account1.setUsername("username2");
        account1.setProfilename("profilename2");
        account1.setPassword("password666121");
        account1.setDescription("Jee jee");

        // save to repository
        accountService.createAccount(account);
        accountService.createAccount(account1);

        // find account by profilename
        Account found = accountService.findByUsername("username2");
        assertTrue(found.getProfilename().equals("profilename2"));
    }

    @Test
    public void findAccountByIdWorks() {
        // create two accounts
        Account account = new Account();
        account.setUsername("username");
        account.setProfilename("profilename");
        account.setPassword("password666");
        account.setDescription("Jee jee");

        // create two accounts
        Account account1 = new Account();
        account1.setUsername("username2");
        account1.setProfilename("profilename2");
        account1.setPassword("password666121");
        account1.setDescription("Jee jee");

        // save to repository
        accountService.createAccount(account);
        accountService.createAccount(account1);

        // get first account
        Account target = accountRepository.findAll().get(0);

        // find account by profilename
        Account found = accountService.findById(target.getId());
        assertTrue(found.getId() == target.getId());
    }

    @Test
    public void listByProfilenameWorks() {
        // create 12 test accounts
        createXAccounts(12);
        
        // check that empty profilename returns 10 accounts
        assertTrue(accountService.listByProfilename("").size() == 10);
        
        // check that matched account is returned
        assertTrue(accountService.listByProfilename("profilename6").get(0).getProfilename().equals("profilename6"));
    }

    private void createXAccounts(int x) {
        for (int i = 0; i < x; i++) {
            Account account = new Account();
            account.setUsername("username" + i);
            account.setProfilename("profilename" + i);
            account.setPassword("password" + i);
            accountService.createAccount(account);
        }
    }

}
