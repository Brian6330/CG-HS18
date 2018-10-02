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

#include "Plane.h"
#include "SolveQuadratic.h"

#include <limits>
#include <array>


//== CLASS DEFINITION =========================================================



Plane::Plane(const vec3& _center, const vec3& _normal)
: center(_center), normal(_normal)
{
}


//-----------------------------------------------------------------------------


bool
Plane::
intersect(const Ray& _ray,
          vec3&      _intersection_point,
          vec3&      _intersection_normal,
          double&    _intersection_t ) const
{
    /** \todo
     * - compute the intersection of the plane with `_ray`
     * - if ray and plane are parallel there is no intersection
     * - otherwise compute intersection data and store it in `_intersection_point`, `_intersection_normal`, and `_intersection_t`.
     * - return whether there is an intersection in front of the viewer (t > 0)
    */

    // SELF-IMPLEMENTATION
    const vec3 &direction = _ray.direction;
    const vec3    &origin = _ray.origin;
    const vec3     offset = center - origin;

    const double angle = dot(normal, direction);

    // is perpendicular
    if (abs(angle) <= 0.0001)
        return false;


    _intersection_t = dot(normal, offset) / angle;
    _intersection_point = origin + _intersection_t*direction;
    _intersection_normal = direction - 2*angle*normal;

    return _intersection_t > 0;

    // todo: maybe more efficient? :
    // _intersection_t is only negative, if angle < 0
    /*
     * // combining "intersection behind the viewer" and "parallel"
     * if (angle <= 0.0001) return false;
     *
     * _intersection_t = dot(normal, offset) / angle;
     * _intersection_point = origin + _intersection_t*direction;
     * _intersection_normal = direction - 2*angle*normal;
     *
     * return true;
     */
}


//=============================================================================
