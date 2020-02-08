package projekti.service;

import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekti.entity.Account;
import projekti.entity.FriendRequest;
import projekti.repository.FriendRequestRepository;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AccountService accountSearchService;

    /**
     * Search friend requests that account has sent and that are not accepted
     * yet.
     *
     * @param sender
     * @return list of friend requests
     */
    @Transactional
    public List<FriendRequest> searchNonAcceptedBySender(Account sender) {
        if (sender == null) {
            return null;
        }
        return friendRequestRepository.findBySenderAndAccepted(sender, false);
    }

    /**
     * Search friend requests that account has received but has not accepted
     * yet.
     *
     * @param receiver
     * @return list of friend requests
     */
    @Transactional
    public List<FriendRequest> searchNonAcceptedByReceiver(Account receiver) {
        if (receiver == null) {
            return null;
        }
        return friendRequestRepository.findByReceiverAndAccepted(receiver, false);
    }

    /**
     * Search friend request between two person. If no request exists between
     * accounts, null is returned.
     *
     * @param person1
     * @param person2
     * @return friend request
     */
    @Transactional
    public FriendRequest searchRequestBetweenTwoAccounts(Account person1, Account person2) {
        // search request where person1 is sender and person2 is receiver
        FriendRequest request = friendRequestRepository.findBySenderAndReceiver(person1, person2);
        if (request != null) {
            return request;
        }
        // search request where person2 is sender and person1 is receiver
        return friendRequestRepository.findBySenderAndReceiver(person2, person1);
    }

    /**
     * Create new friend request bewteen accounts. Friend request is created
     * only if no previous request between accounts exists.
     *
     * @param receiver profilename
     */
    @Transactional
    public void createNewFriendRequest(String receiver) {
        // search account by receiver
        Account receiver1 = accountSearchService.findByProfilename(receiver);
        Account sender = accountSearchService.findByUsername(authenticationService.getUsername());
        if (receiver1 == null || sender == null) {
            return;
        }
        // search existing friendrequest between sender and receiver
        FriendRequest request = searchRequestBetweenTwoAccounts(receiver1, sender);
        if (request != null) {
            return;
        }
        request = new FriendRequest(sender, receiver1, false, LocalDateTime.now());
        friendRequestRepository.save(request);
    }

    /**
     * Delete friend request by id. Request is deleted only if user is eihter
     * sender or receiver.
     *
     * @param id
     */
    @Transactional
    public void deleteById(long id) {
        String username = authenticationService.getUsername();
        FriendRequest request = friendRequestRepository.getOne(id);
        if (request == null) {
            return;
        }
        String receiver = request.getReceiver().getUsername();
        String sender = request.getSender().getUsername();
        // can be deleted if user is receiver or sender of request
        if (receiver.equals(username) || sender.equals(username)) {
            friendRequestRepository.deleteById(id);
        }
    }

    /**
     * Accept friend request. Request is accpeted only if user is receiver.
     *
     * @param id
     */
    @Transactional
    public void acceptById(long id) {
        String username = authenticationService.getUsername();
        FriendRequest request = friendRequestRepository.getOne(id);
        if (request == null) {
            return;
        }
        if (request.getReceiver().getUsername().equals(username)) {
            request.setAccepted(true);
            friendRequestRepository.save(request);
        }
    }

}
