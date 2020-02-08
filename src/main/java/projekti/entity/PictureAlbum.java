
package projekti.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PictureAlbum extends AbstractPersistable<Long>{
    
    @OneToOne(mappedBy = "pictureAlbum")
    private Account owner;
    
    @OneToMany(mappedBy = "pictureAlbum")
    private List<Picture> pictures;
}
