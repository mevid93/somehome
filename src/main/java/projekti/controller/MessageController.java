package projekti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.entity.Account;
import projekti.entity.Message;
import projekti.service.AccountService;
import projekti.service.AuthenticationService;
import projekti.service.MessageService;

@Controller
public class MessageController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

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
