//=============================================================================
//
//   Exercise code for "Introduction to Computer Graphics"
//     by Julian Panetta, EPFL
//
//=============================================================================
#version 140

// Eye-space fragment position and normals from vertex shader.
in vec3 v2f_normal;
in vec3 v2f_ec_vertex;

uniform vec3 light_position; // Eye-space light position
uniform vec3 light_color;

// Material parameters
uniform vec3  diffuse_color;
uniform vec3 specular_color;
uniform float shininess;

uniform samplerCube shadow_map; // Distances in the shadow map can be accessed with texture(shadow_map, direction).r

out vec4 f_light_contribution;

void main()
{
    // Normalize the interpolated normal
    vec3 N = -sign(dot(v2f_normal, v2f_ec_vertex)) *  // Orient the normal so it always points opposite the camera rays
             normalize(v2f_normal);

    /** Done
    * Compute this light's diffuse and specular contributions.
    * You should be able to copy your phong lighting code from assignment 6 mostly as-is,
    * though notice that the light and view vectors need to be computed from scratch
    * here; this time, they are not passed from the vertex shader.
    *
    * The light should only contribute to this fragment if the fragment is not occluded
    * by another object in the scene. You need to check this by comparing the distance
    * from the fragment to the light against the distance recorded for this
    * light ray in the shadow map.
    *
    * To prevent "shadow acne" and minimize aliasing issues, we need a rather large
    * tolerance on the distance comparison. It's recommended to use a *multiplicative*
    * instead of additive tolerance: compare the fragment's distance to 1.01x the
    * distance from the shadow map.
    ***/

    //vec3 color = vec3(0.0f);
    float diffuse = 0.0;
    float specular = 0.0;

    vec3 vecLightToVertex = v2f_ec_vertex - light_position;
    vec3 vecVertexToLight = light_position - v2f_ec_vertex;



    // normalize directions
    vec3 L = normalize(vecVertexToLight);
    vec3 V = normalize(v2f_ec_vertex);
    vec3 R = normalize(reflect(-L, N));

    // Check if observed point is in shade.
    if( length( vecLightToVertex ) < 1.01 * texture(shadow_map, vecLightToVertex).r){

            // Add diffuse and specular light
            diffuse  = max(0.0, dot(N,L));
            specular = (diffuse != 0.0) ? pow(max(0.0, dot(V,R)), shininess) : 0.0;
        }


    vec3 color = diffuse*diffuse_color*light_color + specular*specular_color*light_color;

    // append the required alpha value
    f_light_contribution = vec4(color, 1.0);
}
