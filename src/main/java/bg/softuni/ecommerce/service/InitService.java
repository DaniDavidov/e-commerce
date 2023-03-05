package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.model.entity.UserRoleEntity;
import bg.softuni.ecommerce.model.entity.enums.UserRoleEnum;
import bg.softuni.ecommerce.repository.UserRepository;
import bg.softuni.ecommerce.repository.UserRoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InitService {
    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private PasswordEncoder passwordEncoder;
    private String defaultPassword;

    public InitService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       PasswordEncoder passwordEncoder,
                       @Value("${app.default.password}") String defaultPassword) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultPassword = defaultPassword;
    }

    @PostConstruct
    public void init() {
        initRoles();
        initUsers();
    }


    private void initRoles() {
        if (userRoleRepository.count() == 0) {

            List<UserRoleEntity> collect = Arrays.stream(UserRoleEnum.values()).map(UserRoleEntity::new).collect(Collectors.toList());
            this.userRoleRepository.saveAll(collect);
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            initAdmin();
            initModerator();
            initUser();
        }
    }

    private void initAdmin() {
        UserEntity adminUser = new UserEntity();
        adminUser.setEmail("admin@example.com");
        adminUser.setUsername("admin");
        adminUser.setFirstName("Admin");
        adminUser.setLastName("Adminov");
        adminUser.setPassword(passwordEncoder.encode(defaultPassword));
        adminUser.setUserRoles(userRoleRepository.findAll());

        this.userRepository.save(adminUser);
    }

    private void initModerator() {
        UserRoleEntity userRole = this.userRoleRepository.findByName(UserRoleEnum.MODERATOR);

        UserEntity moderator = new UserEntity();
        moderator.setEmail("moderator@example.com");
        moderator.setUsername("moderator");
        moderator.setFirstName("Moderator");
        moderator.setLastName("Moderatorov");
        moderator.setPassword(passwordEncoder.encode(defaultPassword));
        moderator.setUserRoles(List.of(userRole));

        this.userRepository.save(moderator);
    }

    private void initUser() {

        UserEntity user = new UserEntity();
        user.setEmail("user@example.com");
        user.setUsername("user");
        user.setFirstName("User");
        user.setLastName("Userov");
        user.setPassword(passwordEncoder.encode(defaultPassword));

        this.userRepository.save(user);
    }
}
