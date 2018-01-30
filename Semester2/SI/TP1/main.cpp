#include <qapplication.h>
#include <QString>
#include <stdio.h>
#include <stdlib.h>
#include "viewer.h"

int main(int argc,char** argv) {
  QApplication application(argc,argv);

  Viewer viewer;

  viewer.setWindowTitle("Exercice 01 - sceensaver");
  viewer.show();
  
  return application.exec();
}
