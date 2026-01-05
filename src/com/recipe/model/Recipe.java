package com.recipe.model;

public class Recipe {

    private int id;
    private String title;
    private int userId;
    private String author;
    private int likeCount;
    private String profilePic;   // 👤 profile photo URL/path

    public Recipe(
            int id,
            String title,
            int userId,
            String author,
            int likeCount,
            String profilePic
    ) {
        this.id = id;
        this.title = title;
        this.userId = userId;
        this.author = author;
        this.likeCount = likeCount;
        this.profilePic = profilePic;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getUserId() {
        return userId;
    }

    public String getAuthor() {
        return author;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getAuthorProfilePic() {
        return profilePic;
    }
}