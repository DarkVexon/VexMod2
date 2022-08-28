#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;
uniform float shift_amt;

vec4 rgba(vec2 offset) {
    return v_color * texture2D(u_texture, v_texCoords + offset);
}

vec4 shiftHue(vec3 col, float Shift)
{
    vec3 P = vec3(0.55735) * dot(vec3(0.55735), col);
    vec3 U = col - P;
    vec3 V = cross(vec3(0.55735), U);
    col = U * cos(Shift * 6.2832) + V * sin(Shift * 6.2832) + P;
    return vec4(col, 1.0);
}

void main() {
    vec4 outputColor = rgba(vec2(0, 0));

    if (outputColor.b > outputColor.g && outputColor.b > outputColor.r) {
        vec4 newColor = shiftHue(outputColor.rgb, shift_amt);
        gl_FragColor = vec4(newColor.r, newColor.g, newColor.b, outputColor.a);
    }
    else {
        gl_FragColor = vec4(outputColor.r, outputColor.g, outputColor.b, outputColor.a);
    }


}