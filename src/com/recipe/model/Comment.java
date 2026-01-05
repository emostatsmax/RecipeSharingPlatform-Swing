package com.recipe.model;

public class Comment {

    private int id;
    private int userId;          // ✅ REQUIRED FOR OWNERSHIP
    private String userName;
    private String text;
    private Integer parentId;

    // ✅ MAIN CONSTRUCTOR (USED BY DAO)
    public Comment(int id, int userId, String userName, String text, Integer parentId) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.text = text;
        this.parentId = parentId;
    }

    // ✅ BACKWARD COMPATIBILITY (OLD CODE SAFE)
    public Comment(int id, String userName, String text, Integer parentId) {
        this.id = id;
        this.userName = userName;
        this.text = text;
        this.parentId = parentId;
    }

    public int getId() {
        return id;
    }

    // ✅ REQUIRED FOR DELETE / PERMISSION CHECK
    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public Integer getParentId() {
        return parentId;
    }

    @Override
    public String toString() {
        if (parentId != null) {
            return "   ↳ " + userName + ": " + text;
        }
        return userName + ": " + text;
    }
}