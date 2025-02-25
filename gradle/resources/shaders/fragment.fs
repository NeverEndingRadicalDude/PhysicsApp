#version 400 core

in vec3 colour;

out vec4 fragcolour;

void main() {
    fragcolour = vec4(colour.r, colour.g, colour.b, 1.0);
}