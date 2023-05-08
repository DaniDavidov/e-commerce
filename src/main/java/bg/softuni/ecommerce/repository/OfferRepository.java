package bg.softuni.ecommerce.repository;

import bg.softuni.ecommerce.model.entity.OfferEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<OfferEntity, Long> {

    Page<OfferEntity> findAllByBrandId(Long brandId, Pageable pageable);

    @Query("SELECT o FROM OfferEntity o WHERE o.isApproved = false")
    Page<OfferEntity> findAllUnapprovedOffers(Pageable pageable);
    @Query("SELECT o FROM OfferEntity o WHERE o.isApproved = true")
    Page<OfferEntity> findAllApprovedOffers(Pageable pageable);
}
