package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.user.UserDetailsDto;
import bg.softuni.ecommerce.model.dto.user.UserProfileDto;
import bg.softuni.ecommerce.model.dto.user.UserRegisterDto;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.repository.UserRepository;
import bg.softuni.ecommerce.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       PasswordEncoder passwordEncoder,
                       UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    public void register(UserRegisterDto userRegisterDto) {
        UserEntity user = map(userRegisterDto);
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));

        this.userRepository.save(user);

    }

    public UserEntity getUserByUsername(String username) {
        return this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("No such user"));
    }

    public UserEntity getUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new RuntimeException("No such user"));
    }

    public UserDetailsDto getUserDetails(UserEntity user) {
        return new UserDetailsDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber());
    }

    private UserEntity map(UserRegisterDto userRegisterDto) {
        return new UserEntity(
                userRegisterDto.getUsername(),
                userRegisterDto.getEmail(),
                userRegisterDto.getPassword(),
                userRegisterDto.getFirstName(),
                userRegisterDto.getLastName(),
                userRegisterDto.getPhoneNumber(),
                userRegisterDto.getGender());
    }

    public UserProfileDto getUserProfile(UserEntity user) {
        return new UserProfileDto(
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getAddress());
    }
}
