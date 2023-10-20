package somehome.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Comment entity.
 */
@Data
@EqualsAndHashCode(callSuper = false)
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
