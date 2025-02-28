package com.core;
import com.render.EngineManager;
import com.render.TestGame;
import com.render.WindowManager;

public class Main {


    private static WindowManager window;
    private static TestGame game;
    private static EngineManager engine;
    private static PhysicsManager physics;

    public static void main(String[] args) {

        
        window = new WindowManager(90, "Test", 800, 600, false, false);
        game = new TestGame();
        engine = new EngineManager();
        physics = new PhysicsManager(args);
        

        try {
            engine.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        
        window.cleanup();


        

    }

    public static WindowManager getWindow() {
        return window;
    }

    public static TestGame getEngine() {
        return game;
    }
}