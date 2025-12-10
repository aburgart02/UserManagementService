DROP DATABASE IF EXISTS ums;
CREATE DATABASE ums;
USE ums;

CREATE TABLE last_visit (
    last_visit_id INT AUTO_INCREMENT PRIMARY KEY,
    visit_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    last_visit_id INT,
    FOREIGN KEY (last_visit_id) REFERENCES last_visit(last_visit_id)
);

CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL
);

CREATE TABLE users_has_roles (
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

INSERT INTO last_visit (visit_date) VALUES
(NOW()),
(NOW() - INTERVAL 1 DAY),
(NOW() - INTERVAL 5 HOUR);

INSERT INTO users (username, email, last_visit_id) VALUES
('Ivan_User', 'ivan@test.com', 1),
('Petr_Admin', 'petr@test.com', 2),
('Sidor_Manager', 'sidor@test.com', 3);

INSERT INTO roles (role_name) VALUES
('ROLE_USER'),
('ROLE_ADMIN'),
('ROLE_MANAGER');

INSERT INTO users_has_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO users_has_roles (user_id, role_id) VALUES (2, 2), (2, 1);
INSERT INTO users_has_roles (user_id, role_id) VALUES (3, 3);