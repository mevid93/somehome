package somehome.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Friend request entity.
 */
@Data
@EqualsAndHashCode(callSuper = false)
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
