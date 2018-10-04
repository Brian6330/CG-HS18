//=============================================================================
//
//   Exercise code for the lecture
//   "Introduction to Computer Graphics"
//   by Prof. Dr. Mario Botsch, Bielefeld University
//
//   Copyright (C) Computer Graphics Group, Bielefeld University.
//
//=============================================================================

//== INCLUDES =================================================================

#include "Cylinder.h"
#include "SolveQuadratic.h"

#include <array>
#include <cmath>

//== IMPLEMENTATION =========================================================

bool
Cylinder::
intersect(const Ray&  _ray,
          vec3&       _intersection_point,
          vec3&       _intersection_normal,
          double&     _intersection_t) const
{
    /** \todo
     * - compute the first valid intersection `_ray` with the cylinder
     *   (valid means in front of the viewer: t > 0)
     * - store intersection point in `_intersection_point`
     * - store ray parameter in `_intersection_t`
     * - store normal at _intersection_point in `_intersection_normal`.
     * - return whether there is an intersection with t > 0
    */

    const vec3    &dir = _ray.direction;
    const vec3 &origin = _ray.origin;
    const vec3  offset = origin - center;

    // first coefficient
    const vec3   a = dir - dot(dir, axis)*axis;
    const double A = dot(a, a);

    // second coefficient
    const vec3   b = offset - dot(offset, axis)*axis;
    const double B = 2 * dot(a, b);

    // third coefficient
    const double C = dot(b, b) - pow(radius, 2);

    std::array<double, 2> t;
    size_t nsol = solveQuadratic(A, B, C, t);
    _intersection_t = NO_INTERSECTION;

    // Find the closest valid solution (in front of the viewer)
    for (size_t i = 0; i < nsol; ++i) {
        if (t[i] > 0) _intersection_t = std::min(_intersection_t, t[i]);
    }

    if (_intersection_t == NO_INTERSECTION) return false;

    _intersection_point = origin + _intersection_t*dir;

    const double center_to_intersection = norm(_intersection_point - center);
    const double      h = sqrt(pow(radius, 2) - pow(center_to_intersection, 2));
    _intersection_normal = normalize(_intersection_point -(center + h*normalize(axis)));

    const vec3 rename_me = _intersection_point - _intersection_normal;
    return std::abs(norm(rename_me - center)) <= height / 2;

    // fixme: if ray enters the inner cylinder, it still uses the outer normal vector. This one should be inverted. 

}
