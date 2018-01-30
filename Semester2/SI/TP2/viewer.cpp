#include "viewer.h"

#include <math.h>
#include <iostream>

using namespace std;

float spin_Sat; 

Viewer::Viewer(const QGLFormat &format,const QString &)
  : QGLWidget(format),
    _timer(new QTimer(this)),
    _quadric(NULL),
    _drawMode(false),
    _spin(0.0f),
    _speed(0.1f) {

      spin_Sat = 0.0f;

  _timer->setInterval(10);
  connect(_timer,SIGNAL(timeout()),this,SLOT(updateGL()));
}

Viewer::~Viewer() {
  delete _timer;
  gluDeleteQuadric(_quadric);
}

void Viewer::drawSphere(const glm::vec3 &color) {
  glColor3fv(&color[0]);
  gluSphere(_quadric,1.0f,20,20);
}

void Viewer::setMatrices(const glm::mat4 &model, const glm::mat4 &view, const glm::mat4 &projection) {
  // tells OpenGL to use these model/view/projection matrices 
  glm::mat4 mdv = view*model;

  glMatrixMode(GL_PROJECTION);  // go to the projection mode 
  glLoadMatrixf(&projection[0][0]);  // load the corresponding matrix
  
  glMatrixMode(GL_MODELVIEW); // go to the modelview mode 
  glLoadMatrixf(&mdv[0][0]); // load the corresponding matrix

  // note that model and view matrices are combined here, because 
  // the view can actually be placed in the first level of the stack. 
  // this is the OpenGL way of manipulating matrices 
}

void Viewer::drawScene() {
  // copy the previous matrix
  _modelMatrices.push(_modelMatrices.top());

  // eventually, apply some transformationss to the current (top) model matrix
  // ... 

  // we will use these 3 matrices for the next drawings
  // _modelMatrices.top() = glm::translate(_modelMatrices.top(), glm::vec3(0.0f,0.0f,1.0f));
  _modelMatrices.top() = glm::rotate(_modelMatrices.top(),_spin,glm::vec3(0.0f,1.0f,0.0f));   
  setMatrices(_modelMatrices.top(),_viewMatrix,_projMatrix);
  // draw something (a sphere centered at 0 with radius 1)
  drawSphere(glm::vec3(1.0f,1.0f,0.0f));

  /* Satellite rouge */

  spin_Sat = fmod(spin_Sat+0.01f,360.0f);
  glm::mat4 model_Satellite = glm::scale(_modelMatrices.top(),glm::vec3(0.2f, 0.2f, 0.2f));
  model_Satellite = glm::rotate(model_Satellite,spin_Sat,glm::vec3(0.0f,0.0f,1.0f)); 
  model_Satellite = glm::translate(model_Satellite,glm::vec3(0.0f, 0.0f, 10.0f));

  _modelMatrices.push(model_Satellite);

  setMatrices(_modelMatrices.top(),_viewMatrix,_projMatrix);

  drawSphere(glm::vec3(1.0f,0.0f,0.0f));

  
  _modelMatrices.pop();

  // back to the previous model matrix state
  _modelMatrices.pop();
}

void Viewer::paintGL() {
  // clear the color and depth buffer
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  // set the viewport (covering the entire window)
  glViewport(0,0,width(),height());

  // initialize view and projection matrices here
  _viewMatrix = glm::lookAt(glm::vec3 (0.0f,5.0f,5.0f),glm::vec3 (0.0f,0.0f,0.0f),glm::vec3 (0.0f,1.0f,0.0f));
  _projMatrix = glm::perspective(0.60f,(float)width()/(float)height(),0.8f,100.0f);

  // draw the scene 
  drawScene();

  // the _spin variable may be used to animate spheres
  _spin = fmod(_spin+_speed,360.0f);
}

void Viewer::keyPressEvent(QKeyEvent *ke) {
  
  // key w: wire/filled
  if(ke->key()==Qt::Key_W) {
    if(!_drawMode) 
      glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
    else 
      glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
    
    _drawMode = !_drawMode;
  } 

  // key a: play/stop animation
  if(ke->key()==Qt::Key_A) {
    if(_timer->isActive()) 
      _timer->stop();
    else 
      _timer->start();
  }
  

  updateGL();
}

void Viewer::initializeGL() {
  // make this window the current one
  makeCurrent();

  // init and chack glew
  if(glewInit()!=GLEW_OK) {
    cerr << "Warning: glewInit failed!" << endl;
  }

  if(!GLEW_ARB_vertex_program   ||
     !GLEW_ARB_fragment_program ||
     !GLEW_ARB_texture_float    ||
     !GLEW_ARB_draw_buffers     ||
     !GLEW_ARB_framebuffer_object) {
    cerr << "Warning: Shaders not supported!" << endl;
  }

  // init OpenGL settings
  glClearColor(0.0,0.0,0.0,1.0);
  glEnable(GL_DEPTH_TEST);
  glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);

  // init the glu quadric for drawing spheres
  _quadric = gluNewQuadric();

  // init model,view and projection with identity matrices
  _modelMatrices.push(glm::mat4(1.0f));
  _viewMatrix = glm::mat4(1.0f);
  _projMatrix = glm::mat4(1.0f);
  
  // starts the timer 
  _timer->start();
}

