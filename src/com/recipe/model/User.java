package com.recipe.model;

import com.recipe.util.ProfileDefaults;

public class User {

    private int id;
    private String name;
    private String email;
    private String role;
    private String profilePic;

    // ================= CONSTRUCTORS =================

    // Full constructor (used by login)
    public User(int id, String name, String email, String role, String profilePic) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.profilePic = profilePic;
    }

    // Backward compatibility (old code safe)
    public User(int id, String name, String email, String role) {
        this(id, name, email, role, null);
    }

    // ================= ID =================

    public int getId() {
        return id;
    }

    // Old code compatibility
    public int getUserId() {
        return id;
    }

    // ================= NAME =================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // ================= EMAIL =================

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ================= ROLE =================

    public String getRole() {
        return role;
    }

    // ================= PROFILE PIC =================

    public String getProfilePic() {
        return (profilePic == null || profilePic.isBlank())
                ? ProfileDefaults.DEFAULT_PROFILE_PIC
                : profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}