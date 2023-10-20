package somehome.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import somehome.entity.Account;
import somehome.entity.Message;
import somehome.service.AccountService;
import somehome.service.AuthenticationService;
import somehome.service.MessageService;

/**
 * Message controller class.
 */
@Controller
public class MessageController {

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private AccountService accountService;

  @Autowired
  private MessageService messageService;

  /**
   * Create message.
   *
   * @param message message
   * @param receiverId receiver
   * @return redirect
   */
  @PostMapping("/messages")
  public String create(@RequestParam String message, @RequestParam long receiverId) {
    Account sender = authenticationService.getUserAccount();
    Account receiver = accountService.findById(receiverId);
    messageService.createMessage(sender, receiver, message);
    if (receiver == null || sender.getId() == receiverId) {
      return "redirect:/home";
    }
    return "redirect:/accounts/" + receiver.getProfilename();
  }

  /**
   * Like message.
   *
   * @param id id
   * @return redirect
   */
  @PostMapping("/messages/{id}/like")
  public String like(@PathVariable long id) {
    Account account = authenticationService.getUserAccount();
    Message message = messageService.getMessageById(id);
    messageService.createLike(account, message);
    if (message == null || account.getId() == message.getReceiver().getId()) {
      return "redirect:/home";
    }
    return "redirect:/accounts/" + message.getReceiver().getProfilename();
  }

  /**
   * Comment message.
   *
   * @param id id
   * @param comment comment
   * @return redirect
   */
  @PostMapping("/messages/{id}/comment")
  public String comment(@PathVariable long id, @RequestParam String comment) {
    Account account = authenticationService.getUserAccount();
    Message message = messageService.getMessageById(id);
    messageService.createComment(account, message, comment);
    if (message == null || account.getId() == message.getReceiver().getId()) {
      return "redirect:/home";
    }
    return "redirect:/accounts/" + message.getReceiver().getProfilename();
  }

}
