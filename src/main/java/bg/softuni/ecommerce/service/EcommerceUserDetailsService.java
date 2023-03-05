package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.model.entity.UserRoleEntity;
import bg.softuni.ecommerce.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class EcommerceUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public EcommerceUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.userRepository
                .findByUsername(username)
                .map(this::mapUserEntity)
                .orElseThrow(() -> new UsernameNotFoundException("No such user."));
    }

    private UserDetails mapUserEntity(UserEntity userEntity) {
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                extractAuthorities(userEntity));
    }

    private List<GrantedAuthority> extractAuthorities(UserEntity userEntity) {
        return userEntity.getUserRoles()
                .stream()
                .map(this::mapRole)
                .collect(Collectors.toList());
    }

    private GrantedAuthority mapRole(UserRoleEntity userRole) {
        return new SimpleGrantedAuthority("ROLE_" + userRole.getName().name());
    }


}
