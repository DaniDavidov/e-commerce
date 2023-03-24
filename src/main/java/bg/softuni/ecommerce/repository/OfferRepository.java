package bg.softuni.ecommerce.repository;

import bg.softuni.ecommerce.model.entity.OfferEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<OfferEntity, Long> {

    Page<OfferEntity> findAllByItemBrandId(Long brandId, Pageable pageable);
}
