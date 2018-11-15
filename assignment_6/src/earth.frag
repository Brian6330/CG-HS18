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

uniform sampler2D day_texture;
uniform sampler2D night_texture;
uniform sampler2D cloud_texture;
uniform sampler2D gloss_texture;
uniform bool greyscale;

const float shininess = 20.0;
const vec3  sunlight = vec3(1.0, 0.941, 0.898);

void main()
{

    // normalize directions
    vec3 N = normalize(v2f_normal);
    vec3 L = normalize(v2f_light);
    vec3 V = normalize(v2f_view);
    vec3 R = normalize(reflect(-L, N));


    // compute diffuse and specular intensities
    float diffuse  = max(0.0, dot(N,L));
    float specular = (diffuse != 0.0) ? pow(max(0.0, dot(V,R)), shininess) : 0.0;


    // fetch textures
    vec3  day   = texture(day_texture,    v2f_texcoord.st).rgb;
    vec3  night = texture(night_texture,  v2f_texcoord.st).rgb;
    float cloudiness = texture(cloud_texture,  v2f_texcoord.st).r;
    float gloss = texture(gloss_texture,  v2f_texcoord.st).r;


    // clouds are not specular
    gloss *= (1.0 - cloudiness);


    // combine textures with lighting following the Phong lighting model (specular material component is [1, 1, 1])
    day = 0.2 * sunlight * day + diffuse * sunlight * day + specular * gloss * sunlight;
    day = mix(day, 0.2 * sunlight * vec3(cloudiness) + diffuse * sunlight * vec3(cloudiness), cloudiness);
    night *= (1.0 - cloudiness);

    // mix day and night based on diffuse dot product
    vec3 color = mix(night, day, clamp(2.0*diffuse, 0.0, 1.0));


    // convert RGB color to YUV color and use only the luminance
    if (greyscale) color = vec3(0.299*color.r+0.587*color.g+0.114*color.b);


    // add required alpha value
    f_color = vec4(color, 1.0);

}
