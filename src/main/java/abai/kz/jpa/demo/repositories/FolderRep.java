package abai.kz.jpa.demo.repositories;

import jakarta.transaction.Transactional;
import abai.kz.jpa.demo.entities.Folders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface FolderRep extends JpaRepository<Folders, Long> {
}
