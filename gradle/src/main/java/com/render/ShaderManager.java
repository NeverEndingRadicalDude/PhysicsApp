package com.render;

import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import java.util.Map;
import java.util.HashMap;

public class ShaderManager {

    private final int programID;
    private int vertexShaderID, fragmentShaderID;
    private final Map<String, Integer> uniforms;

    public ShaderManager() throws Exception {
        programID = GL20.glCreateProgram();

        System.out.println("" + programID);
        if (programID == 0) {
            throw new Exception("Could not create shader");
        }
        uniforms = new HashMap<>();
        
    }

    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = GL20.glGetUniformLocation(programID, uniformName);
        if (uniformLocation < 0) {
            throw new Exception("could not find uniform " + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            GL20.glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallacFloat(16)));
        }
    }

    public void setUniform(String uniformName, int value) {
        GL20.glUniform1i(uniforms.get(uniformName), value);
    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderID = createShader(shaderCode, GL20.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderID = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
    }

    public int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderID = GL20.glCreateShader(shaderType);
        if (shaderID == 0) 
            throw new Exception("Error creating shader, shader type : " + shaderType);
        
        GL20.glShaderSource(shaderID, shaderCode);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0) 
            throw new Exception("Error compiling shader code, TYPE: " + shaderType + " info " + GL20.glGetShaderInfoLog(shaderID, 1024));
        
        GL20.glAttachShader(programID, shaderID);
        
        return shaderID;
    }

    public void link() throws Exception {
        
        
        if (vertexShaderID == 0) {
            GL20.glDetachShader(programID, vertexShaderID);
            System.out.println("Error with vertexShader");
        }

        if (fragmentShaderID == 0) {
            GL20.glDetachShader(programID, fragmentShaderID);
            System.out.println("Error with fragmentShader");
        }

        GL20.glLinkProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0)
            throw new Exception("Error linking shader code: " + GL20.glGetShaderInfoLog(programID, 1024));
        
        if (vertexShaderID != 0) {
            GL20.glDetachShader(programID, vertexShaderID);
        }

        if (fragmentShaderID != 0) {
            GL20.glDetachShader(programID, fragmentShaderID);
        }

        GL20.glValidateProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0) {
             throw new Exception("Unable to validate shader code : " + GL20.glGetProgramInfoLog(programID, 1024));
        }
    }

    public void bind() {
        GL20.glUseProgram(programID);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programID != 0) {
            GL20.glDeleteProgram(programID);
        }
    }
}