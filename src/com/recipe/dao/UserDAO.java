package com.recipe.dao;

import com.recipe.model.User;
import com.recipe.util.DBUtil;
import com.recipe.util.ProfileDefaults;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // ================= LOGIN =================
    public User login(String email, String password) {
        try (Connection c = DBUtil.getConnection()) {

            PreparedStatement ps = c.prepareStatement(
                    "SELECT * FROM users WHERE email=? AND password=?"
            );
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("profile_pic")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================= REGISTER (FIXED) =================
    public void register(String name, String email, String password) {
        try (Connection c = DBUtil.getConnection()) {

            PreparedStatement ps = c.prepareStatement(
                    """
                    INSERT INTO users (name, email, password, role, profile_pic)
                    VALUES (?, ?, ?, 'USER', ?)
                    """
            );

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, ProfileDefaults.DEFAULT_PROFILE_PIC);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= UPDATE PROFILE =================
    public void updateProfile(
            int userId,
            String name,
            String email,
            String password,
            String profilePic
    ) {
        try (Connection c = DBUtil.getConnection()) {

            PreparedStatement ps = c.prepareStatement(
                    """
                    UPDATE users
                    SET name=?, email=?, password=?, profile_pic=?
                    WHERE id=?
                    """
            );

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, profilePic);
            ps.setInt(5, userId);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}