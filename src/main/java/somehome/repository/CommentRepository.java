
package somehome.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import somehome.entity.Comment;
import somehome.entity.Picture;

/**
 * Comment repository class.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> findByPicture(Picture picture, Pageable pageable);

}
