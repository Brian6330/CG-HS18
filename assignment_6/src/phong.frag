//=============================================================================
//
//   Exercise code for the lecture "Introduction to Computer Graphics"
//     by Prof. Mario Botsch, Bielefeld University
//
//   Copyright (C) by Computer Graphics Group, Bielefeld University
//
//=============================================================================

#version 140

in vec3 v2f_normal;
in vec2 v2f_texcoord;
in vec3 v2f_light;
in vec3 v2f_view;

out vec4 f_color;

uniform sampler2D tex;
uniform bool greyscale;

const float shininess = 8.0;
const vec3  sunlight = vec3(1.0, 0.941, 0.898);

void main()
{

    vec3 color = vec3(0.0,0.0,0.0);

    // normalize directions
    vec3 N = normalize(v2f_normal);
    vec3 L = normalize(v2f_light);
    vec3 V = normalize(v2f_view);
    vec3 R = normalize(reflect(-L, N));

    // compute diffuse and specular intensities
    float ambient  = 0.2;
    float diffuse  = max(0.0, dot(N,L));
    float specular = (diffuse != 0.0) ? pow(max(0.0, dot(V,R)), shininess) : 0.0;

    // fetch textures
    vec3 material = texture(tex, v2f_texcoord.st).rgb;

    // combine material (=texture) with lighting
    color = ambient*material*sunlight + diffuse*material*sunlight + specular*material*sunlight;

    // convert RGB color to YUV color and use only the luminance
    if (greyscale) color = vec3(0.299*color.r+0.587*color.g+0.114*color.b);

    // add required alpha value
    f_color = vec4(color, 1.0);
}
