package somehome.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Picture entity.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Picture extends AbstractPersistable<Long> {

  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] content;

  private String description;
  private String name;
  private String mediaType;
  private long size;
  private boolean profilePicture;
  private long likeCounter;

  @ManyToOne
  private PictureAlbum pictureAlbum;

  @OneToMany(mappedBy = "picture", fetch = FetchType.LAZY)
  private List<MyLike> myLikes;
}
