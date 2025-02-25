#version 400 core

in vec2 fragTextureCoord;

out vec4 fragcolour;

uniform sampler2D textureSampler;

void main() {
    fragcolour = texture(textureSampler, fragTextureCoord);
}