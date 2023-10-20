
package somehome.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;


/**
 * Picture album entity.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PictureAlbum extends AbstractPersistable<Long> {

  @OneToOne(mappedBy = "pictureAlbum")
  private Account owner;

  @OneToMany(mappedBy = "pictureAlbum")
  private List<Picture> pictures;
}
