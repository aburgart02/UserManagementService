package com.university.usermanagementservice.dao;
import com.university.usermanagementservice.dto.User;
import java.util.List;
import java.util.Map;

public interface UserRepository {
    Map<Integer, User> getAllUsers();

    User getUserById(int userId);

    List<String> getRolesByUserId(int userId);
}