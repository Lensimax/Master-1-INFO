#include "viewer.h"

#include <math.h>
#include <iostream>

using namespace std;

Viewer::Viewer(const QGLFormat &format,const QString &)
  : QGLWidget(format),
    _timer(this) {

  _timer.setInterval(10);
  connect(&_timer,SIGNAL(timeout()),this,SLOT(updateGL()));
}

void Viewer::generateRandomPoints() {
  Point p;

  for(int i=0;i<=500;i++){
  	p.pos=randv2(-1.0,1.0);
  	p.dir=randv2(-0.01,0.01);
  	p.col=randv3(0.0,1.0);
  	p.size=randf(1.0,10.0);

  	_points.push_back(p);

  }


}

void Viewer::updatePointPositions() {
 	for (std::vector<Point>::iterator it = _points.begin() ; it != _points.end(); ++it){
 		if(it->pos.x < -1){
			it->dir = glm::reflect(it->dir,glm::vec2(1,0));
 		} else if(it->pos.x > 1){
			it->dir = glm::reflect(it->dir,glm::vec2(1,0));
		} else if(it->pos.y < -1){
			it->dir = glm::reflect(it->dir,glm::vec2(0,1));
		} else if(it->pos.y > 1){
			it->dir = glm::reflect(it->dir,glm::vec2(0,1));
 		} 
 		it->pos += it->dir;
 	} 
}

void Viewer::paintGL() {
  // clear color and depth buffers 
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

  glBegin(GL_TRIANGLE_STRIP);
  glColor3f(1.0,0.0,0.0);
  glVertex3f(-1,1,0);
  glColor3f(1.0,1.0,0.0);
  glVertex3f(-1,-1,0);
  glVertex3f(-0.5,1,0);
  glVertex3f(0.5,-1,0);
  glColor3f(0.0,1.0,0.0);
  glVertex3f(1,1,0);
  glColor3f(0.0,0.0,1.0);
  glVertex3f(1,-1,0);
  glEnd();


  for (std::vector<Point>::iterator it = _points.begin() ; it != _points.end(); ++it){
  	glPointSize(it->size);
  	glBegin(GL_POINTS);
  	glColor3f(it->col.x,it->col.y,it->col.z);
  	glVertex3f(it->pos.x,it->pos.y,0.0);
  	glEnd();

  }

  updatePointPositions();


}

void Viewer::resizeGL(int width,int height) {
	glViewport(0,0,width,height);
}

void Viewer::keyPressEvent(QKeyEvent *ke) {
  
  // key a: play/stop animation
  if(ke->key()==Qt::Key_A) {
    if(_timer.isActive()) 
      _timer.stop();
    else 
      _timer.start();
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
  glDisable(GL_DEPTH_TEST);
  glPolygonMode(GL_FRONT_AND_BACK,GL_FILL);
  glEnable(GL_POINT_SMOOTH);

  // initialize points 
  generateRandomPoints();
  
  // starts the timer 
  _timer.start();
}

