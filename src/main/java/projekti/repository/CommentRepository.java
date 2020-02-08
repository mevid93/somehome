
package projekti.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.entity.Comment;
import projekti.entity.Picture;


public interface CommentRepository extends JpaRepository<Comment, Long>{
    
    List<Comment> findByPicture(Picture picture, Pageable pageable);
    
}
