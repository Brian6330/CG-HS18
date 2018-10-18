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

#include "Mesh.h"
#include <fstream>
#include <string>
#include <stdexcept>
#include <limits>
#include <array>


//== IMPLEMENTATION ===========================================================


Mesh::Mesh(std::istream &is, const std::string &scenePath)
{
	std::string meshFile, mode;
	is >> meshFile;

	// load mesh from file
	read(scenePath.substr(0, scenePath.find_last_of('/') + 1) + meshFile);

	is >> mode;
	if      (mode ==  "FLAT") draw_mode_ = FLAT;
	else if (mode == "PHONG") draw_mode_ = PHONG;
	else throw std::runtime_error("Invalid draw mode " + mode);

	is >> material;
}


//-----------------------------------------------------------------------------


bool Mesh::read(const std::string &_filename)
{
	// read a mesh in OFF format


	// open file
	std::ifstream ifs(_filename);
	if (!ifs)
	{
		std::cerr << "Can't open " << _filename << "\n";
		return false;
	}


	// read OFF header
	std::string s;
	unsigned int nV, nF, dummy, i;
	ifs >> s;
	if (s != "OFF")
	{
		std::cerr << "No OFF file\n";
		return false;
	}
	ifs >> nV >> nF >> dummy;
	std::cout << "\n  read " << _filename << ": " << nV << " vertices, " << nF << " triangles";


	// read vertices
	Vertex v;
	vertices_.clear();
	vertices_.reserve(nV);
	for (i=0; i<nV; ++i)
	{
		ifs >> v.position;
		vertices_.push_back(v);
	}


	// read triangles
	Triangle t;
	triangles_.clear();
	triangles_.reserve(nF);
	for (i=0; i<nF; ++i)
	{
		ifs >> dummy >> t.i0 >> t.i1 >> t.i2;
		triangles_.push_back(t);
	}


	// close file
	ifs.close();


	// compute face and vertex normals
	compute_normals();

	// compute bounding box
	compute_bounding_box();


	return true;
}


//-----------------------------------------------------------------------------

// Determine the weights by which to scale triangle (p0, p1, p2)'s normal when
// accumulating the vertex normals for vertices 0, 1, and 2.
// (Recall, vertex normals are a weighted average of their incident triangles'
// normals, and in our raytracer we'll use the incident angles as weights.)
// \param[in] p0, p1, p2    triangle vertex positions
// \param[out] w0, w1, w2    weights to be used for vertices 0, 1, and 2
void angleWeights(const vec3 &p0, const vec3 &p1, const vec3 &p2,
                  double &w0, double &w1, double &w2) {
	// compute angle weights
	const vec3 e01 = normalize(p1-p0);
	const vec3 e12 = normalize(p2-p1);
	const vec3 e20 = normalize(p0-p2);
	w0 = acos( std::max(-1.0, std::min(1.0, dot(e01, -e20) )));
	w1 = acos( std::max(-1.0, std::min(1.0, dot(e12, -e01) )));
	w2 = acos( std::max(-1.0, std::min(1.0, dot(e20, -e12) )));
}


//-----------------------------------------------------------------------------

void Mesh::compute_normals()
{
	// initialize vertex normals to zero
	for (Vertex& v: vertices_)
	{
		v.normal = vec3(0,0,0);
	}

	// compute triangle normals
	// compute vertex normals
	for (Triangle& t : triangles_)
	{
		const vec3& p0 = vertices_[t.i0].position;
		const vec3& p1 = vertices_[t.i1].position;
		const vec3& p2 = vertices_[t.i2].position;
		t.normal = normalize(cross(p1-p0, p2-p0));

		double w0=0, w1=0, w2=0;
		Vertex& v0 = vertices_[t.i0], v1 = vertices_[t.i1], v2 = vertices_[t.i2];
		angleWeights(v0.position, v1.position, v2.position, w0, w1, w2);

		v0.normal += w0*t.normal;
		v1.normal += w1*t.normal;
		v2.normal += w2*t.normal;
	}

	for (Vertex& v : vertices_) {
		v.normal = normalize(v.normal);
	}
}


//-----------------------------------------------------------------------------


void Mesh::compute_bounding_box()
{
	bb_min_ = vec3(std::numeric_limits<double>::max());
	bb_max_ = vec3(std::numeric_limits<double>::lowest());

	for (Vertex v: vertices_)
	{
		bb_min_ = min(bb_min_, v.position);
		bb_max_ = max(bb_max_, v.position);
	}
}


//-----------------------------------------------------------------------------


bool Mesh::intersect_bounding_box(const Ray& _ray) const
{

    /** \todo
    * Intersect the ray `_ray` with the axis-aligned bounding box of the mesh.
    * Note that the minimum and maximum point of the bounding box are stored
    * in the member variables `bb_min_` and `bb_max_`. Return whether the ray
    * intersects the bounding box.
    * This function is ued in `Mesh::intersect()` to avoid the intersection test
    * with all triangles of every mesh in the scene. The bounding boxes are computed
    * in `Mesh::compute_bounding_box()`.
    */
	float tmin=0, tmax=0, tymin=0, tymax=0, tzmin=0, tzmax=0;

	if (_ray.direction[0] >= 0) {
		tmin = (bb_min_[0] - _ray.origin[0]) / _ray.direction[0];
		tmax = (bb_max_[0] - _ray.origin[0]) / _ray.direction[0];
	}
	else {
		tmin = (bb_max_[0] - _ray.origin[0]) / _ray.direction[0];
		tmax = (bb_min_[0] - _ray.origin[0]) / _ray.direction[0];
	}

	if (_ray.direction[1] >= 0) {
		tymin = (bb_min_[1] - _ray.origin[1]) / _ray.direction[1];
		tymax = (bb_max_[1] - _ray.origin[1]) / _ray.direction[1];
	}
	else {
		tymin = (bb_max_[1] - _ray.origin[1]) / _ray.direction[1];
		tymax = (bb_min_[1] - _ray.origin[1]) / _ray.direction[1];
	}

	if ((tmin > tymax) || (tymin > tmax))
		return false;

	if (tymin > tmin)
		tmin = tymin;

	if (tymax < tmax)
		tmax = tymax;

	if (_ray.direction[2] >= 0) {
		tzmin = (bb_min_[2] - _ray.origin[2]) / _ray.direction[2];
		tzmax = (bb_max_[2] - _ray.origin[2]) / _ray.direction[2];
	}
	else {
		tmin = (bb_max_[2] - _ray.origin[2]) / _ray.direction[2];
		tmax = (bb_min_[2] - _ray.origin[2]) / _ray.direction[2];
	}

	if ((tmin > tzmax) || (tzmin > tmax))
		return false;

	return true;
}


//-----------------------------------------------------------------------------


bool Mesh::intersect(const Ray& _ray,
                     vec3&      _intersection_point,
                     vec3&      _intersection_normal,
                     double&    _intersection_t ) const
{
	// check bounding box intersection
	if (!intersect_bounding_box(_ray))
	{
		return false;
	}

	vec3   p, n;
	double t;

	_intersection_t = NO_INTERSECTION;

	// for each triangle
	for (const Triangle& triangle : triangles_)
	{
		// does ray intersect triangle?
		if (intersect_triangle(triangle, _ray, p, n, t))
		{
			// is intersection closer than previous intersections?
			if (t < _intersection_t)
			{
				// store data of this intersection
				_intersection_t      = t;
				_intersection_point  = p;
				_intersection_normal = n;
			}
		}
	}

	return (_intersection_t != NO_INTERSECTION);
}


//-----------------------------------------------------------------------------


bool
Mesh::
intersect_triangle(const Triangle&  _triangle,
                   const Ray&       _ray,
                   vec3&            _intersection_point,
                   vec3&            _intersection_normal,
                   double&          _intersection_t) const
{
	const vec3& p0 = vertices_[_triangle.i0].position;
	const vec3& p1 = vertices_[_triangle.i1].position;
	const vec3& p2 = vertices_[_triangle.i2].position;

	const vec3& dir = _ray.direction;

	vec3 edge1 = p1-p0;
	vec3 edge2 = p2-p0;

	vec3 plane_vec = cross(dir, edge2);
	const double det = dot(edge1, plane_vec);

	// behind viewer or parallel
	if (fabs(det) > std::numeric_limits<double>::min()) return false;

	const double det_inv = 1.0 / det;

	vec3 tvec = _ray.origin - p0;
	double hitbox_norm_edge2 = dot(tvec, plane_vec) * det_inv;

	// hitting normalized hitbox
	if (hitbox_norm_edge2 < 0.0 || hitbox_norm_edge2 > 1.0) return false;

	vec3 qvec = cross(tvec, edge1);
	double hitbox_norm_edge1 = dot(dir, qvec) * det_inv;

	// hitting normalized hitbox
	// limited to edge3 hitbox
	if (hitbox_norm_edge1 < 0 || hitbox_norm_edge1+hitbox_norm_edge2 > 1) return false;

	_intersection_t = dot(edge2, qvec) * det_inv;
	_intersection_point = _ray.origin + dir * _intersection_t;


	_intersection_normal = cross(edge1, edge2);
	if (dot(_intersection_normal, dir) < 0)
		_intersection_normal *= -1.0;

	return true;

}


//=============================================================================
