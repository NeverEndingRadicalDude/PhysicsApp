package com.render;

import com.core.Main;
import com.render.entity.Model;

public class TestGame implements ILogic {

    private int direction = 0;
    private float color = 0.0f;

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;

    private Model model;

    public TestGame() {
        renderer = new RenderManager();
        window = Main.getWindow();
        loader = new ObjectLoader();
    }

    public void init() throws Exception {
        renderer.init();

        float[] verticies = {
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f,
            -0.5f, 0.5f, 0f
        };

        int[] indicies = {
            1, 2, 3,
            3, 1, 2
        };

        float[] textureCoords = {
            0,0,
            0,1,
            1,1,
            1,0
        };

        model = loader.loadModel(verticies, textureCoords, indicies);
        model.setTexture(new Texture(loader.loadTexture("textures/grassblock.png")));
    }

    public void input() {
        
    }

    public void update() {
        window.update();
    }

    public void render() {
        renderer.render(model);
    }

    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }

}