package com.recipe.dao;

import com.recipe.model.Recipe;
import com.recipe.model.Comment;
import com.recipe.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {

    // ===================== ADD RECIPE =====================
    public void addRecipe(String title, int userId) {
        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO recipes (title, user_id) VALUES (?, ?)"
            );
            ps.setString(1, title);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== GET ALL RECIPES (FEED) =====================
    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();

        try (Connection c = DBUtil.getConnection()) {

            String sql = """
                SELECT r.id,
                       r.title,
                       r.user_id,
                       u.name,
                       COALESCE(u.profile_pic, '') AS profile_pic,
                       (SELECT COUNT(*) FROM likes l WHERE l.recipe_id = r.id) AS likes
                FROM recipes r
                JOIN users u ON r.user_id = u.id
                ORDER BY r.id DESC
            """;

            ResultSet rs = c.createStatement().executeQuery(sql);

            while (rs.next()) {
                list.add(new Recipe(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getInt("likes"),
                        rs.getString("profile_pic")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===================== LIKE / UNLIKE =====================
    public void toggleLike(int recipeId, int userId) {
        try (Connection c = DBUtil.getConnection()) {

            PreparedStatement check = c.prepareStatement(
                    "SELECT id FROM likes WHERE recipe_id=? AND user_id=?"
            );
            check.setInt(1, recipeId);
            check.setInt(2, userId);

            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                PreparedStatement del = c.prepareStatement(
                        "DELETE FROM likes WHERE recipe_id=? AND user_id=?"
                );
                del.setInt(1, recipeId);
                del.setInt(2, userId);
                del.executeUpdate();
            } else {
                PreparedStatement ins = c.prepareStatement(
                        "INSERT INTO likes (recipe_id, user_id) VALUES (?, ?)"
                );
                ins.setInt(1, recipeId);
                ins.setInt(2, userId);
                ins.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== COMMENTS =====================
    public List<Comment> getComments(int recipeId) {
        List<Comment> list = new ArrayList<>();

        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    """
                    SELECT c.id, c.user_id, u.name, c.comment, c.parent_id
                    FROM comments c
                    JOIN users u ON c.user_id = u.id
                    WHERE c.recipe_id = ?
                    ORDER BY COALESCE(c.parent_id, c.id), c.id
                    """
            );
            ps.setInt(1, recipeId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Comment(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("comment"),
                        (Integer) rs.getObject("parent_id")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ===================== ADD COMMENT =====================
    public void addComment(int recipeId, int userId, String text) {
        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO comments (recipe_id, user_id, comment) VALUES (?,?,?)"
            );
            ps.setInt(1, recipeId);
            ps.setInt(2, userId);
            ps.setString(3, text);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== ADD REPLY =====================
    public void addReply(int recipeId, int userId, int parentCommentId, String text) {
        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO comments (recipe_id, user_id, comment, parent_id) VALUES (?,?,?,?)"
            );
            ps.setInt(1, recipeId);
            ps.setInt(2, userId);
            ps.setString(3, text);
            ps.setInt(4, parentCommentId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== DELETE COMMENT =====================
    public void deleteComment(int commentId, int userId) {
        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                    "DELETE FROM comments WHERE id=? AND user_id=?"
            );
            ps.setInt(1, commentId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===================== DELETE RECIPE =====================
    public void deleteRecipe(int recipeId) {
        try (Connection c = DBUtil.getConnection()) {

            c.prepareStatement("DELETE FROM likes WHERE recipe_id=" + recipeId).executeUpdate();
            c.prepareStatement("DELETE FROM comments WHERE recipe_id=" + recipeId).executeUpdate();
            c.prepareStatement("DELETE FROM recipes WHERE id=" + recipeId).executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}