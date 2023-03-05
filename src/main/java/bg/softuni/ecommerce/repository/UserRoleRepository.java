package bg.softuni.ecommerce.repository;

import bg.softuni.ecommerce.model.entity.UserRoleEntity;
import bg.softuni.ecommerce.model.entity.enums.UserRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    UserRoleEntity findByName(UserRoleEnum name);
}
