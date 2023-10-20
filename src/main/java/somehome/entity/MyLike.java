
package somehome.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;


/**
 * MyLike entity.
 */
@Data
@EqualsAndHashCode(callSuper = false)
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
