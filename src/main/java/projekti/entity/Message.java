package projekti.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message extends AbstractPersistable<Long> {

    @NotEmpty
    @Size(max = 200)
    private String content;
    
    @ManyToOne
    private Account sender;
    
    @ManyToOne
    private Account receiver;
    
    private LocalDateTime time;
    
    private long likeCounter;
    
    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
    private List<MyLike> myLikes;
    
    @OneToMany(mappedBy = "message", fetch = FetchType.LAZY)
    private List<Comment> comments;
    
}
