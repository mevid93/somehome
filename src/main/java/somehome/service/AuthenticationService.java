package somehome.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import somehome.entity.Account;
import somehome.repository.AccountRepository;

/**
 * Authentication service class.
 */
@Service
public class AuthenticationService {

  @Autowired
  private AccountRepository accountRepository;

  /**
   * Check if current user is authenticated. Only users who have role "USER"
   * are authenticated.
   *
   * @return true if authenticated
   */
  public boolean isAuthenticated() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      return false;
    }
    for (GrantedAuthority a : auth.getAuthorities()) {
      if (a.getAuthority().equals("USER")) {
        return true;
      }
    }
    return false;
  }

  /**
   * Return Username for current user.
   *
   * @return profilename
   */
  public String getUsername() {
    if (!isAuthenticated()) {
      return "";
    }
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }

  /**
   * Get account for authenticated user.
   *
   * @return account
   */
  @Transactional
  public Account getUserAccount() {
    String username = this.getUsername();
    return accountRepository.findByUsername(username);
  }
}
