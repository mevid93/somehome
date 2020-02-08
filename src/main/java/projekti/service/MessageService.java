package projekti.service;

import java.time.LocalDateTime;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import projekti.entity.Account;
import projekti.entity.Comment;
import projekti.entity.Message;
import projekti.entity.MyLike;
import projekti.repository.CommentRepository;
import projekti.repository.MessageRepository;
import projekti.repository.MyLikeRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private FriendService friendService;

    @Autowired
    private MyLikeRepository myLikeRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Create message from sender to receiver.
     *
     * @param sender
     * @param receiver
     * @param msg
     */
    @Transactional
    public void createMessage(Account sender, Account receiver, String msg) {
        // validate input
        if (sender == null || receiver == null || msg.isEmpty()) {
            return;
        }
        // make sure that accounts are friends or that account is sending message itself
        int status = friendService.areAccountsFriends(sender, receiver);
        if (sender.getId() != receiver.getId() && status != 2) {
            return;
        }
        // create message
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(msg);
        message.setTime(LocalDateTime.now());
        // save to repository
        messageRepository.save(message);
    }

    /**
     * Get 25 latest messages for given account.
     *
     * @param account
     * @return list of latest messages
     */
    @Transactional
    public List<Message> getLatestWallMessages(Account account) {
        Pageable pageable = PageRequest.of(0, 25, Sort.by("time").descending());
        return messageRepository.findByReceiver(account, pageable);
    }

    /**
     * Get message by id.
     *
     * @param id
     * @return message
     */
    @Transactional
    public Message getMessageById(long id) {
        return this.messageRepository.getOne(id);
    }

    /**
     * Create like from account to message. Only if there was no previous like
     * for message.
     *
     * @param account
     * @param message
     */
    @Transactional
    public void createLike(Account account, Message message) {
        // validate input
        if (account == null || message == null) {
            return;
        }
        // make sure that account has not liked message before
        MyLike like = myLikeRepository.findByAccountAndMessage(account, message);
        if (like != null) {
            return;
        }
        // make sure that account is owner of the message or that account
        // is friend of the message reveicer
        if (friendService.areAccountsFriends(account, message.getReceiver()) != 2 && account.getId() != message.getReceiver().getId()) {
            return;
        }
        // create new like
        like = new MyLike();
        like.setAccount(account);
        like.setMessage(message);
        myLikeRepository.save(like);
        message.setLikeCounter(message.getLikeCounter() + 1);
        messageRepository.save(message);
    }

    /**
     * Create comment from account to message.
     *
     * @param account
     * @param message
     * @param msg
     */
    @Transactional
    public void createComment(Account account, Message message, String msg) {
        // validate input
        if (account == null || message == null || msg == null || msg.isEmpty() || msg.length() > 200) {
            return;
        }
        // make sure that account is owner of the message or that account
        // is friend of the picture owner
        Account owner = message.getReceiver();
        if (friendService.areAccountsFriends(account, owner) != 2 && account.getId() != owner.getId()) {
            return;
        }
        // create new comment
        Comment comment = new Comment();
        comment.setAccount(account);
        comment.setMessage(message);
        comment.setContent(msg);
        comment.setTime(LocalDateTime.now());
        commentRepository.save(comment);
    }

}
