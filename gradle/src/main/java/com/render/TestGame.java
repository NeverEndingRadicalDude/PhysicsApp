package com.render;

import com.core.Main;
import com.render.entity.Model;

public class TestGame implements ILogic {

    private int direction = 0;
    private float color = 0.0f;

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;

    private Entity entity;

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
            0.5f, 0.5f, 0f
        };

        int[] indicies = {
            0, 1, 3,
            3, 1, 2
        };

        float[] textureCoords = {
            0,0,
            0,1,
            1,1,
            1,0
        };

        Model model = loader.loadModel(verticies, textureCoords, indicies);
        model.setTexture(new Texture(loader.loadTexture("textures/grassblock.png")));

        entity = new Entity(model, new Vector3f(1, 0, 0), new Vector(0, 0, 0), 1);
    }

    public void input() {
        
    }

    public void update() {
        color += direction * 0.01f;
        if(color > 1)
            color = 1.0f;
        else if(color <= 0)
            color = 0.0f;
        
        if(entity.getPos() < -1.5f)
            entity.getPos().x = 1.5f;
        
        entity.getPos().x -= 0.01f;
    }

    public void render() {
        if (window.isResize()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }

        window.setClearColor(color, color, color, 0.0f);
        renderer.render(entity);
    }

    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }

}