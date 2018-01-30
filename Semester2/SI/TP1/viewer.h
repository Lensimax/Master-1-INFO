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

#include <QGLFormat>
#include <QGLWidget>
#include <QMouseEvent>
#include <QKeyEvent>
#include <QTimer>
#include <stack>

typedef struct Point {
  glm::vec2 pos; // position
  glm::vec2 dir; // direction
  glm::vec3 col; // COLOR
  float size;    // size 
} Point;


class Viewer : public QGLWidget {
 public:
  Viewer(const QGLFormat &format=QGLFormat::defaultFormat(),
	 const QString &filename=QString());

 protected :
  virtual void paintGL();
  virtual void initializeGL();
  virtual void keyPressEvent(QKeyEvent *ke);
  virtual void resizeGL(int width,int height);

 private:
  inline float randf(float min,float max);      // return a random scalar value in the range [min,max]
  inline glm::vec2 randv2(float min,float max); // return a random 2D vector with values in the range [min,max]
  inline glm::vec3 randv3(float min,float max); // return a random 3D vector with values in the range [min,max]

  // generate a vector of points with randomized directions, colors and sizes
  void generateRandomPoints();

  // update point positions and direction for animation
  void updatePointPositions();

  std::vector<Point> _points; // a set of points 
  QTimer         _timer;      // timer that controls the animation
};

float Viewer::randf(float min,float max) {
  // return a random scalar value in the range [min,max]
  return (((float)rand())/(float)RAND_MAX)*(max-min) + min;
}

glm::vec2 Viewer::randv2(float min,float max) {
  // return a random 2D vector with values in the range [min,max]
  return glm::vec2(randf(min,max),randf(min,max));
}

glm::vec3 Viewer::randv3(float min,float max) {
  // return a random 3D vector with values in the range [min,max]
  return glm::vec3(randf(min,max),randf(min,max),randf(min,max));
}


#endif // VIEWER_H
