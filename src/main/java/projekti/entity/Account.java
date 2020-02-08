package projekti.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
