package bg.softuni.ecommerce.service;

import bg.softuni.ecommerce.model.dto.user.UserDetailsDto;
import bg.softuni.ecommerce.model.dto.user.UserProfileDto;
import bg.softuni.ecommerce.model.dto.user.UserRegisterDto;
import bg.softuni.ecommerce.model.entity.UserEntity;
import bg.softuni.ecommerce.model.entity.UserRoleEntity;
import bg.softuni.ecommerce.model.entity.enums.UserRoleEnum;
import bg.softuni.ecommerce.model.error.UserNotFoundException;
import bg.softuni.ecommerce.repository.UserRepository;
import bg.softuni.ecommerce.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public Page<UserDetailsDto> getAllUsers(Pageable pageable) {
        return this.userRepository.findAll(pageable).map(this::getUserDetails);
    }

    public UserDetailsDto getUserDetails(UserEntity user) {
        return new UserDetailsDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getUserRoles()
                        .stream()
                        .map(userRole -> userRole.getName().name())
                        .collect(Collectors.toList()));
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

    public void promoteToModerator(Long userId) {
        UserRoleEntity roleEntity = this.userRoleRepository.findByName(UserRoleEnum.valueOf("MODERATOR"));

        UserEntity user = getUserById(userId);
        user.setUserRoles(new ArrayList<>(List.of(roleEntity)));
        this.userRepository.save(user);
    }

    public void demoteToUser(Long userId) {
        UserRoleEntity roleEntity = this.userRoleRepository.findByName(UserRoleEnum.valueOf("USER"));

        UserEntity user = getUserById(userId);
        user.setUserRoles(new ArrayList<>(List.of(roleEntity)));
        this.userRepository.save(user);
    }
}
