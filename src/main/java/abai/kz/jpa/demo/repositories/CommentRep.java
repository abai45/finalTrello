package abai.kz.jpa.demo.repositories;

import abai.kz.jpa.demo.entities.Tasks;
import jakarta.transaction.Transactional;
import abai.kz.jpa.demo.entities.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface CommentRep extends JpaRepository<Comments, Long> {
    List<Comments> findByTask(Tasks tasks);

}
