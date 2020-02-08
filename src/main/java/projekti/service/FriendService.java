package projekti.service;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekti.entity.Account;
import projekti.entity.FriendRequest;
import projekti.repository.FriendRequestRepository;

@Service
public class FriendService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;
    
    @Autowired
    private FriendRequestService friendRequestService;

    /**
     * Return list of friends for given account.
     *
     * @param account
     * @return friendlist
     */
    @Transactional
    public List<Account> findFriendsForAccount(Account account) {
        if (account == null) {
            return null;
        }
        return friendRequestRepository.findFriendsForAccount(account);
    }

    /**
     * Delete friendship bewteen two accounts.
     *
     * @param user
     * @param friend
     */
    @Transactional
    public void deleteFriendship(Account user, Account friend) {
        // make sure that accounts exist
        if (user == null || friend == null) {
            return;
        }
        // find their friendship information
        FriendRequest request = friendRequestService.searchRequestBetweenTwoAccounts(user, friend);
        // check that not null and that 
        if (request != null) {
            friendRequestRepository.deleteById(request.getId());
        }
    }

    /**
     * Returns information about two users friendship. Return 0 if accounts are
     * not friend and no friendrequests exists. Return 1 if accounts are not
     * friend but friendrequest exist. Return 2 if accounts are friends.
     *
     * @param account1
     * @param account2
     * @return
     */
    @Transactional
    public int areAccountsFriends(Account account1, Account account2) {
        if (account1 == null || account2 == null) {
            return 0;
        }
        // find their friendship information
        FriendRequest request = friendRequestService.searchRequestBetweenTwoAccounts(account1, account2);
        if (request == null) {
            return 0;
        }
        return (request.isAccepted()) ? 2 : 1;
    }

}
