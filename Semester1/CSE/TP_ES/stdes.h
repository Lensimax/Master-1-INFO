#ifndef _STDES_H
#define _STDES_H

#define BUFFER_SIZE 1024

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>



struct _ES_FICHIER {
	int fd;

	int position;
	int indice_buffer;
	int nbRestant_buffer; // nombre d'element encore a lire

	char *buffer;


};

typedef struct _ES_FICHIER FICHIER;

FICHIER *ouvrir(char *nom, char mode);
int fermer(FICHIER*f);
int lire(void *p, unsigned int taille, unsigned int nbelem, FICHIER *f);
int ecrire(const void *p, unsigned int taille, unsigned int nbelem, FICHIER *f);
int fecriref(FICHIER *fp, char*format, ...);
int fliref(FICHIER *fp, char*format, ...);

#endif

