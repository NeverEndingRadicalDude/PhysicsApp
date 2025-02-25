package com.render;

import org.lwjgl.Version;

import com.render.WindowManager;

public class Launcher {
    public static void main(String[] args) {
        System.out.println(Version.getVersion());
        WindowManager window = new WindowManager(90f, "TestWindow", 1600, 900, true, false);
        window.init();

        while (!window.windowShouldClose()) {
            window.update();
        }

        window.cleanup();

    }
}