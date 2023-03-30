package bg.softuni.ecommerce.repository;

import bg.softuni.ecommerce.model.entity.BlacklistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface BlacklistRepository extends JpaRepository<BlacklistEntity, Long> {


    Optional<BlacklistEntity> findByUserUsername(String username);


    @Transactional
    void deleteByUserUsername(String username);
}

