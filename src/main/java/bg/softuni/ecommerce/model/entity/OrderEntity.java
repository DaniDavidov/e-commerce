package bg.softuni.ecommerce.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;


@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @ManyToOne(optional = false)
    private UserEntity owner;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "orders_items",
            joinColumns = {@JoinColumn(name = "order_id", referencedColumnName = "id")})
    @MapKeyJoinColumn(name = "offer_id")
    @Column(name = "quantity")
    private Map<OfferEntity, Integer> items;

    private boolean isProcessed;

    public OrderEntity(UserEntity owner, Map<OfferEntity, Integer> items) {
        this.owner = owner;
        setItems(items);
        this.isProcessed = false;
    }

    public void setItems(Map<OfferEntity, Integer> items) {
        this.items = new LinkedHashMap<>();
        this.items.putAll(items);
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public Map<OfferEntity, Integer> getItems() {
        return items;
    }

    public boolean isProcessed() {
        return isProcessed;
    }
}
