
package projekti.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MyLike extends AbstractPersistable<Long> {
    
    // account who has liked
    @ManyToOne
    private Account account;
    
    // target of the like can be either picture or message
    @ManyToOne
    private Picture picture;
    
    @ManyToOne
    private Message message;
    
}
