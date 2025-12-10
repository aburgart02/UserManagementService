package com.university.usermanagementservice.dao;

import com.university.usermanagementservice.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {
    @Autowired
    private JdbcTemplate template;

    private static final String BASE_QUERY = """
        SELECT u.user_id, u.username, u.email, lv.visit_date, r.role_name
        FROM ums.users u
        LEFT JOIN ums.users_has_roles uhr ON u.user_id = uhr.user_id
        LEFT JOIN ums.roles r ON uhr.role_id = r.role_id
        LEFT JOIN ums.last_visit lv ON u.last_visit_id = lv.last_visit_id
        """;

    @Override
    public Map<Integer, User> getAllUsers() {
        return template.query(BASE_QUERY, new UserResultSetExtractor());
    }

    @Override
    public User getUserById(int userId) {
        String query = BASE_QUERY + " WHERE u.user_id = ?";
        Map<Integer, User> result = template.query(query, new UserResultSetExtractor(), userId);
        if (result == null || result.isEmpty()) {
            return null;
        }
        return result.get(userId);
    }

    @Override
    public List<String> getRolesByUserId(int userId) {
        return template.query(
                """
                SELECT r.role_name 
                FROM ums.roles r 
                JOIN ums.users_has_roles uhr ON r.role_id = uhr.role_id 
                WHERE uhr.user_id = ?
                """,
                (rs, rowNum) -> rs.getString("role_name"),
                userId
        );
    }

    private static class UserResultSetExtractor implements ResultSetExtractor<Map<Integer, User>> {
        @Override
        public Map<Integer, User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> users = new HashMap<>();
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                User user = users.get(userId);
                if (user == null) {
                    user = new User();
                    user.setId(userId);
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setLastVisit(rs.getTimestamp("visit_date"));
                    user.setRoles(new ArrayList<>());
                    users.put(userId, user);
                }
                String roleName = rs.getString("role_name");
                if (roleName != null) {
                    user.getRoles().add(roleName);
                }
            }
            return users;
        }
    }
}