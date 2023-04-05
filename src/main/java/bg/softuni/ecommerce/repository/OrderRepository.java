package bg.softuni.ecommerce.repository;

import bg.softuni.ecommerce.model.entity.OrderEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("SELECT o FROM OrderEntity o WHERE o.isProcessed = true")
    List<OrderEntity> findAllProcessed();
    @Query("SELECT o FROM OrderEntity o WHERE o.isProcessed = false")
    List<OrderEntity> findAllUnprocessed();

}
