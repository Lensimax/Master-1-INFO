#ifndef VIEWER_H
#define VIEWER_H

// GLEW lib: needs to be included first!!
#include <GL/glew.h> 

// OpenGL library 
#include <GL/gl.h>

// OpenGL Utility library
#include <GL/glu.h>

// OpenGL Mathematics
#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>

#include <QGLFormat>
#include <QGLWidget>
#include <QMouseEvent>
#include <QKeyEvent>
#include <QTimer>
#include <stack>

class Viewer : public QGLWidget {
 public:
  Viewer(const QGLFormat &format=QGLFormat::defaultFormat(),
	 const QString &filename=QString());
  ~Viewer();
  
  void drawScene();

 protected :
  virtual void paintGL();
  virtual void initializeGL();
  virtual void keyPressEvent(QKeyEvent *ke);

 private:
  QTimer        *_timer;    // timer that controls the animation
  GLUquadricObj *_quadric;  // quadric used for drawing the sphere
  bool           _drawMode; // press w for wire or fill drawing mode
  float          _spin;     // current rotation (in degree)
  float          _speed;    // speed added to the spin at each frame

  std::stack<glm::mat4> _modelMatrices; // model matrix (stored in a stack to control hierarchical transformations)
  glm::mat4 _viewMatrix; // view matrix
  glm::mat4 _projMatrix; // projection matrix 

  // draw a simple sphere centered at 0 with radius 1
  void drawSphere(const glm::vec3 &color);

  // tells OpenGL to use these matrices before drawing something
  void setMatrices(const glm::mat4 &model, const glm::mat4 &view, const glm::mat4 &projection);
};

#endif // VIEWER_H
