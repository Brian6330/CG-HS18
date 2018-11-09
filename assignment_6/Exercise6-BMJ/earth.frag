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
const vec3 defaultVec1 = vec3(1, 1, 1);
const vec3 defaultVec0= vec3(0, 0, 0);

void main()
{
    vec3 color = vec3(0.0,0.0,0.0);
    vec3 day = vec3(0.0,0.0,0.0);
    vec3 night = vec3(0.0,0.0,0.0);


    vec3 day_texture_value = texture(day_texture, v2f_texcoord.st).rgb;
    vec3 night_texture_value = texture(night_texture, v2f_texcoord.st).rgb;

    // 1 for water, 0 for other
    vec3 gloss_texture_value = texture(gloss_texture, v2f_texcoord.st).rgb;

    vec3 cloud = vec3(0.0, 0.0, 0.0);
    vec3 cloud_texture_value = texture(cloud_texture, v2f_texcoord.st).rgb;
    float murkiness = length(cloud_texture_value);

    // Ambient: I_a * m_a, I_a fixed to 0.2 * sunlight
    day += 0.2 * sunlight * day_texture_value;
    night += 0.2 * sunlight * night_texture_value;

    // Diffuse: I_l * m_d * <normal, light>
    float diffuse_factor = dot(v2f_normal, v2f_light);
    if (diffuse_factor > 0) {
        day += sunlight * day_texture_value * diffuse_factor;

        // Specular: I_l * m_s * <reflected, view>^s
        vec3 reflected_light = reflect(-v2f_light, v2f_normal);
        float specular_factor = dot(reflected_light, v2f_view);
        if (specular_factor > 0  && gloss_texture_value == defaultVec1) {
            day += (1-murkiness) * sunlight * defaultVec1 * pow(specular_factor, shininess);
        }
    }

    // Ambient: I_a * m_a, I_a fixed to 0.2 * sunlight
    cloud += 0.2 * sunlight * cloud_texture_value;
    // Diffuse: I_l * m_d * <normal, light>
    cloud += sunlight * cloud_texture_value * diffuse_factor;
    // mix is an interpolation
    day = mix(day, cloud, murkiness);
    night = mix(night_texture_value, defaultVec0, murkiness);

    color = mix(night, day, abs((diffuse_factor + 1) / 2));

    // convert RGB color to YUV color and use only the luminance
    if (greyscale) color = vec3(0.299*color.r+0.587*color.g+0.114*color.b);

    // add required alpha value
    f_color = vec4(color, 1.0);
    /** \todo
    * - Copy your working code from the fragment shader of your Phong shader use it as
    * starting point
    * - instead of using a single texture, use the four texures `day_texure`, `night_texure`,
    * `cloud_texure` and `gloss_texture` and mix them for enhanced effects
    * Hints:
    * - cloud and gloss textures are just greyscales. So you'll just need one color-
    * component.
    * - The texture(texture, 2d_position) returns a 4-vector (rgba). You can use
    * `texture(...).r` to get just the red component or `texture(...).rgb` to get a vec3 color
    * value
    * - use mix(vec3 a,vec3 b, s) = a*(1-s) + b*s for linear interpolation of two colors
     */

}
