package somehome.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;


/**
 * Message entity.
 */
@Data
@EqualsAndHashCode(callSuper = false)
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
