package net.enjoy.springboot.fooddelivery.service;

import net.enjoy.springboot.fooddelivery.dto.UserDto;
import net.enjoy.springboot.fooddelivery.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);
    User findUserByEmail(String email);
    List<User> findAllUsers();
    UserDto getUserById(Long id);
    void updateUser(UserDto userDto);
    void deleteUser(Long id);
    void saveAll(List<User> users);
}