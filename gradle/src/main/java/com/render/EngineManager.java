package com.render;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import com.core.Main;

public class EngineManager {
    
    public static final long NANOSECOND = 1000000000L;
    public static final float FRAMERATE = 1000;

    private static int fps;
    private static float frametime = 1.0f / FRAMERATE;

    private boolean isRunning;

    private WindowManager window;
    private GLFWErrorCallback errorCallback;
    private ILogic engineLogic;

    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Main.getWindow();
        System.out.println("window: " + window);
        engineLogic = Main.getEngine();
        System.out.println("engineLogic: " + engineLogic);
        window.init();
        engineLogic.init();
    }

    public void start() throws Exception {
        init();
        if (isRunning) 
            return;
        run();
    }

    public void run() {
        this.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            // input
            input();

            while (unprocessedTime > frametime) {
                render = true;
                unprocessedTime -= frametime;

                if (window.windowShouldClose()) {
                    stop();
                }
                if (frameCounter >= NANOSECOND) {
                    setFps(frames);
                    window.setTitle("Engine Fps: " + getFps());
                    frames = 0;
                    frameCounter = 0;
                }

            }

            if (render) {
                update();
                render();
                frames++;
            }
        }

        cleanup();
    }

    private void stop() {
        if (!isRunning)
            return;
        
        isRunning = false;
    }

    private void input() {
        engineLogic.input();
    }

    private void update() {
        engineLogic.update();
        window.update();
    }

    private void render() {
        window.update();
        engineLogic.render();
    }

    private void cleanup() {
        window.cleanup();
        engineLogic.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public int getFps() {
        return fps;
    }



}