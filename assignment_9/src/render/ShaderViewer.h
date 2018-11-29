
#pragma once

#include "gl.h"
#include "glfw_window.h"

#include "shader.h"
#include "Quad.h"
#include "../utils/vec.h"
#include "../utils/array2d.h"

//=============================================================================

/// OpenGL viewer that handles all the rendering for us
class ShaderViewer : public GLFW_window
{
public:

	/// default constructor
	/// \_title the window's title
	/// \_width the window's width
	/// \_height the window's height
	ShaderViewer(std::string const& _title = "Shader Viewer", int _width=640, int _height=640);

	void setShaderFiles(std::string const& vertex_shader_path, std::string const& fragment_shader_path);
	void setShaderFiles(std::vector<std::string> const& vertex_shader_paths, std::vector<std::string> const& fragment_shader_paths);

	virtual void saveScreenshot(std::string const& path) override;

	virtual void setViewerPosition(vec2 const& position);

	Array2D<vec4> extractFrame();

	/// resize function - called when the window size is changed
	virtual void resize(int width, int height) override;
	
protected:
	/// function that is called on the creation of the widget for the initialisation of OpenGL
	virtual void initialize() override;

	/// paint function - called when the window should be refreshed
	virtual void paint() override;

	virtual void paint_shader_to_texture();

	/// keyboard interaction
	virtual void keyboard(int key, int scancode, int action, int mods) override;

	virtual void scroll_wheel(double xoffset, double yoffset) override;

	void mouse_button(int button, int action, int mods) override;

	void mouse_drag(vec2 pos);

	virtual void timer(double dt) override;

	void save(std::string const& path);

	void zoom(double amount);

private:
	Shader texture_shader;
	Shader shader_to_display;

	Quad fullscreen_quad;

	GLuint fbo = 0;
	GLuint m_render_tex = 0;
    uint32_t m_render_tex_width = 0, m_render_tex_height = 0;

	/// current viewport dimension
	uint32_t  width_ = 0, height_ = 0;
	
	// movement
	bool should_redraw = true;

	vec2 viewer_position = {0.0f, 0.0f};
	vec2 viewer_velocity = {0.0f, 0.0f};
	float zoom_velocity = 0.0;
	float viewer_scale = 1.0;

	bool mouse_pressed = false;
	vec2 last_mouse_pos = {0.0f, 0.0f};
	float time = 0;
	bool have_time = false;
    bool do_animate = false;
};


//=============================================================================

