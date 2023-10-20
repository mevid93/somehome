package somehome.service;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import somehome.entity.Account;
import somehome.repository.AccountRepository;

/**
 * Account service class.
 */
@Service
public class AccountService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private PictureAlbumService pictureAlbumService;

  /**
   * Find list of accounts by profilename. Max 10 accounts are returned. These
   * 10 accounts do not contain user's account. This means user can not search
   * his/her own account from the system.
   *
   * @param profilename profile name
   * @return list of accounts
   */
  @Transactional
  public List<Account> listByProfilename(String profilename) {
    // limit search to 11 first hit and return results
    Pageable pageable = PageRequest.of(0, 11, Sort.by("profilename").ascending());
    List<Account> accounts = accountRepository
        .findByProfilenameContainingIgnoreCase(profilename, pageable);
    // make sure that current users profile was not returned
    String username = authenticationService.getUsername();
    for (int i = accounts.size() - 1; i >= 0; i--) {
      if (accounts.get(i).getUsername().equals(username)) {
        accounts.remove(i);
        break;
      }
    }
    return (accounts.size() <= 10) ? accounts : accounts.subList(0, 10);
  }

  /**
   * Return account found by profilename. If no account was found, null is
   * returned.
   *
   * @param profilename profile name
   * @return account
   */
  @Transactional
  public Account findByProfilename(String profilename) {
    Account account = accountRepository.findByProfilename(profilename);
    return account;
  }

  /**
   * Return account found by username. If no account was found, null is
   * returned.
   *
   * @param username username
   * @return account
   */
  @Transactional
  public Account findByUsername(String username) {
    Account account = accountRepository.findByUsername(username);
    return account;
  }

  /**
   * Return one account corresponding to given id.
   *
   * @param id account id
   * @return account
   */
  @Transactional
  public Account findById(long id) {
    return accountRepository.getReferenceById(id);
  }

  /**
   * Create new account and picture album for it.
   *
   * @param account account
   */
  @Transactional
  public void createAccount(Account account) {
    // parameters were good --> encode the password, save to database and redirect
    // to login page
    account.setPassword(passwordEncoder.encode(account.getPassword()));
    accountRepository.save(account);
    // create picture album for account
    pictureAlbumService.createPictureAlbumIfNotExist(account);
  }

}
