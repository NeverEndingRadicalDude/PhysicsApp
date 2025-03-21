package com.render;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;


public class WindowManager {
    public float fov;
    public static float zNear = 0.01f;
    public static float zFar = 1000f;

    private String title;

    private int width, height;
    private long window;

    private boolean resize, vsync;

    private final Matrix4f projectMatrix;

    public WindowManager(float f, String t, int w, int h, boolean r, boolean v) {
        fov = f;
        title = t;
        width = w;
        height = h;
        resize = r;
        vsync = v;
        projectMatrix = new Matrix4f();
    }

    public void init() {

        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) 
            throw new IllegalStateException("Unable to initialize GLFW");
        
        // set window hints
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

        boolean maximized = false;
        if (width == 0 || height == 0) {
            width = 100;
            height = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
            maximized = true;
        }

        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }

        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResize(true);
        });

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(window, true);
            }
        });

        if(maximized) 
            GLFW.glfwMaximizeWindow(window);

        GLFW.glfwMakeContextCurrent(window);

        if(isVsync())
        GLFW.glfwSwapInterval(1);

        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        //GL11.glEnable(GL11.GL_CULL_FACE);
        //GL11.glCullFace(GL11.GL_BACK);

    }

    public void update() {
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents(); 
    }

    public void cleanup() {
        GLFW.glfwDestroyWindow(window);
    }

    public void setClearColor(float r, float g, float b, float a) {
        GL11.glClearColor(r, g, b, a);
    }

    public boolean isKeyPressed(int keycode) {
        return GLFW.glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window); 
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
    }

    public boolean isVsync() {
        return vsync;
    }

    public void setVsync(boolean vsync) {
        this.vsync = vsync;
    }

    public boolean isResize() {
        return resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getWindow() {
        return window;
    }

    public Matrix4f getProjectionMatrix() {
        return projectMatrix;
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float) width / height;
        return projectMatrix.perspective(fov, aspectRatio, zNear, zFar);
    }

    public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width, int height) {
        float aspectRatio = (float) width / height;
        return matrix.perspective(fov, aspectRatio, zNear, zFar);
    }
}