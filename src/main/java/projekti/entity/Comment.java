package projekti.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends AbstractPersistable<Long> {

    @ManyToOne
    private Account account;
    
    private LocalDateTime time;
    
    @NotEmpty
    @Size(max = 200)
    private String content;
    
    // target of the like can be either picture or message
    @ManyToOne
    private Picture picture;

    @ManyToOne
    private Message message;

}
