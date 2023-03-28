package bg.softuni.ecommerce.repository;

import bg.softuni.ecommerce.model.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByOfferId(Long offerId);

    List<CartEntity> findAllByBuyerUsername(String username);

    @Transactional
    void deleteAllByBuyerId(Long buyerId);
}
