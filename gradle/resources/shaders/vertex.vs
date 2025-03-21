#version 400 core

in vec3 position;
in vec2 textureCoord;

out vec3 colour;
out vec2 fragTextureCoord;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main() {
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
    fragTextureCoord = textureCoord;
    colour = vec3(position.x + 0.5, 0.0, position.y + 0.5);
    
}