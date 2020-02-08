package projekti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entity.Account;
import projekti.entity.Message;
import projekti.entity.MyLike;
import projekti.entity.Picture;

public interface MyLikeRepository extends JpaRepository<MyLike, Long> {
    
    MyLike findByAccountAndMessage(Account account, Message message);
    
    MyLike findByAccountAndPicture(Account account, Picture picture);
    
}
