package projekti.entity;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
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
