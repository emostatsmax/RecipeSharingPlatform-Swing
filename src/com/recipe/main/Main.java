package com.recipe.main;

import com.recipe.ui.LoginFrame;
import com.recipe.util.DatabaseInitializer;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.init();
        new LoginFrame();
    }
}