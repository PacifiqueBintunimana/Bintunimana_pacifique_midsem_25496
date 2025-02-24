package net.enjoy.springboot.fooddelivery.service;

import net.enjoy.springboot.fooddelivery.dto.UserDto;
import net.enjoy.springboot.fooddelivery.entity.Role;
import net.enjoy.springboot.fooddelivery.entity.User;
import net.enjoy.springboot.fooddelivery.repository.RoleRepository;
import net.enjoy.springboot.fooddelivery.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setDateOfBirth(userDto.getDateOfBirth());
// Fetch the role from the repository
        Optional<Role> optionalRole = roleRepository.findByName("ROLE_USER");
        Role role;

        // Check if the role exists, if not, call a method to handle it
        if (optionalRole.isPresent()) {
            role = optionalRole.get();  // Get the Role from Optional
        } else {
            role = checkRoleExist();     // Handle role existence as needed
        }

        user.setRoles(List.of(role));
        userRepository.save(user);
    }
    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
//        return users.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return convertEntityToDto(user);
    }

    @Override
    public void updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setDateOfBirth(userDto.getDateOfBirth());
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setDateOfBirth(user.getDateOfBirth());
        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }

    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> searchUsers(String search, Pageable pageable) {
        return userRepository.findByFirstNameContainingOrLastNameContainingOrEmailContaining(search, search, search, pageable);
    }
    @Override
    public void saveAll(List<User> users) {
        userRepository.saveAll(users); // Assuming `userRepository` supports `saveAll`
    }
}