package somehome.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Account entity.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account extends AbstractPersistable<Long> {

  @NotEmpty
  @Size(min = 5, max = 30)
  private String username;

  @NotEmpty
  @Size(min = 5, max = 30)
  private String profilename;

  @NotEmpty
  @Size(min = 8)
  private String password;

  private String description;

  @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
  private List<FriendRequest> sentFriendRequests;

  @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
  private List<FriendRequest> receivedFriendRequests;

  @OneToOne(fetch = FetchType.LAZY)
  private PictureAlbum pictureAlbum;

  @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
  private List<Message> sentMessages;

  @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
  private List<Message> receivedMessages;

  @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
  private List<MyLike> myLikes;

  @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
  private List<Comment> comments;

}
