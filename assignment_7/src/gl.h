//=============================================================================
//
//   Exercise code for the lecture "Introduction to Computer Graphics"
//     by Prof. Mario Botsch, Bielefeld University
//
//   Copyright (C) by Computer Graphics Group, Bielefeld University
//
//=============================================================================
#ifndef GL_WRAPPERS_HH
#define GL_WRAPPERS_HH
//=============================================================================

// Mac OS X
#ifdef __APPLE__
#  include <glew.h>

// Windows
#elif _WIN32
#  include <stdlib.h>
#  include <glew.h>

// Unix
#else
#  include <glew.h>
#endif

#include <iostream>


//=============================================================================

#define glCheckError() __glCheckError(__FILE__,__LINE__)

/// Check for OpenGL errors.
inline void __glCheckError(const char *file, int line)
{
    GLenum error;
    int num_errors = 0;

    do
    {
        error = glGetError();
        if (error != GL_NO_ERROR) {
            std::cerr << "GL error at " << file << ":" << line << ", :";
            ++num_errors;
        }

        switch (error)
        {
            case GL_INVALID_ENUM:
                std::cerr << " GL error: invalid enum\n";
                break;

            case GL_INVALID_VALUE:
                std::cerr << " GL error: invalid value (out of range)\n";
                break;

            case GL_INVALID_OPERATION:
                std::cerr << " GL error: invalid operation (not allowed in current state)\n";
                break;

            case GL_INVALID_FRAMEBUFFER_OPERATION:
                std::cerr << " GL error: invalid framebuffer operation (framebuffer not complete)\n";
                break;

            case GL_OUT_OF_MEMORY:
                std::cerr << " GL error: out of memory\n";
                break;

            case GL_STACK_UNDERFLOW:
                std::cerr << " GL error: stack underflow\n";
                break;

            case GL_STACK_OVERFLOW:
                std::cerr << " GL error: stack overflow\n";
                break;
            case GL_NO_ERROR:
                break;
            default:
                std::cerr << "unknown enum" << std::endl;
        }
        std::cerr << std::flush;
    }
    while (error != GL_NO_ERROR);
    if(num_errors) std::abort();
}


//=============================================================================
#endif
//=============================================================================
