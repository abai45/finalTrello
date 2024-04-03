package abai.kz.jpa.demo.repositories;

import jakarta.transaction.Transactional;
import abai.kz.jpa.demo.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface CategoryRep extends JpaRepository<Categories, Long> {
}
