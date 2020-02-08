package projekti.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FriendRequest extends AbstractPersistable<Long> {

    @ManyToOne
    private Account sender;
    
    @ManyToOne
    private Account receiver;
    
    private boolean accepted;
    
    private LocalDateTime created;
}
