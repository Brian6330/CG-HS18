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

    // Solve for where _ray intersects an infinite extension of the cylinder
    const vec3 &dir = _ray.direction;
    const vec3   oc = _ray.origin - center;

    const double dir_parallel = dot(axis, dir),
                  oc_parallel = dot(axis, oc);

    std::array<double, 2> t;
    size_t nsol = solveQuadratic(
            dot(dir, dir) - dir_parallel * dir_parallel,
            2.0 * (dot(dir, oc) - dir_parallel * oc_parallel),
            dot(oc, oc) - oc_parallel * oc_parallel - radius * radius, t);

    // Find the closest valid solution
    // (in front of the viewer and within the cylinder's height).
    _intersection_t = NO_INTERSECTION;
    for (size_t i = 0; i < nsol; ++i) {
        if (t[i] <= 0) continue;
        double z = dot(_ray(t[i]) - center, axis);
        if (2 * std::abs(z) < height)
            _intersection_t = std::min(_intersection_t, t[i]);
    }
    if (_intersection_t == NO_INTERSECTION) return false;

    // compute intersection data
    _intersection_point   = _ray(_intersection_t);
    _intersection_normal  = (_intersection_point - center) / radius;
    _intersection_normal -= dot(_intersection_normal, axis) * axis;

    // Choose the normal's orientation to be opposite the ray's
    // (in case the ray intersects the inside surface)
    if (dot(_intersection_normal, dir) > 0)
        _intersection_normal *= -1.0;

    return true;
}
