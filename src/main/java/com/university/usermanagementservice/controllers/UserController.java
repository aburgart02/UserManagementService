package com.university.usermanagementservice.controllers;

import com.university.usermanagementservice.dao.UserRepository;
import com.university.usermanagementservice.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v0")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping("/users")
    public Mono<ResponseEntity<Map<String, Object>>> getAllUsers() {
        Map<String, Object> map = new HashMap<>();
        Map<Integer, User> users = userRepository.getAllUsers();

        map.put("code", 200);
        map.put("message", "success");
        map.put("data", users);
        return Mono.just(ResponseEntity.ok().body(map));
    }

    @GetMapping("/users/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> getUserById(@PathVariable int id) {
        Map<String, Object> map = new HashMap<>();
        User user = userRepository.getUserById(id);

        if (user != null) {
            map.put("code", 200);
            map.put("message", "success");
            map.put("data", user);
            return Mono.just(ResponseEntity.ok().body(map));
        } else {
            map.put("code", 404);
            map.put("message", "User not found");
            return Mono.just(ResponseEntity.status(404).body(map));
        }
    }

    @GetMapping("/users/{id}/roles")
    public Mono<ResponseEntity<Map<String, Object>>> getUserRoles(@PathVariable int id) {
        Map<String, Object> map = new HashMap<>();
        List<String> roles = userRepository.getRolesByUserId(id);

        map.put("code", 200);
        map.put("message", "success");
        map.put("data", roles);
        return Mono.just(ResponseEntity.ok().body(map));
    }
}