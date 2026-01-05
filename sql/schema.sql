CREATE DATABASE IF NOT EXISTS recipe_platform;
USE recipe_platform;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100),
                       email VARCHAR(100),
                       password VARCHAR(100)
);

CREATE TABLE recipes (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         title VARCHAR(255),
                         user_id INT,
                         FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE likes (
                       recipe_id INT,
                       user_id INT,
                       UNIQUE(recipe_id, user_id)
);

CREATE TABLE comments (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          recipe_id INT,
                          user_id INT,
                          text TEXT
);