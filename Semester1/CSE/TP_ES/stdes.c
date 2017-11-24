#include "stdes.h"



FICHIER *ouvrir(char *nom, char mode){
	FICHIER *file;
	struct stat *infos;


	// ouverture fichier
	if(mode == 'L'){ // lecture
		file->fd = open(nom, O_RDONLY);

		if(file->fd == -1){
			perror("[ERROR] Ouverture fichier en lecture");
			return NULL;
		}

	} else if(mode == 'E'){ // écriture
		file->fd = open(nom, O_WRONLY | O_CREAT, S_IRWXU );
		
		if(file->fd == -1){
			perror("[ERROR] Ouverture fichier en écriture");
			return NULL;
		}

	} else {
		fprintf(stderr, "[ERROR] Mode inconnu\n");
		return NULL;
	}

	file->position = 0;
	file->indice_buffer = 0;

	file->buffer = malloc(BUFFER_SIZE);

	return file;

}


int fermer(FICHIER*f){
	free(f->buffer);
	return close(f->fd);
}



int lire(void *p, unsigned int taille, unsigned int nbelem, FICHIER *f){
	
	int nbLu = 0;
	int indice_buffer;
	int n;

	if(taille < 1 || nbelem < 1){
		return -1;
	}

	p = malloc(taille * nbelem);

	while(nbelem - nbLu > 0){ // tant qu'il reste des choses à lire

		if(f->indice_buffer >= BUFFER_SIZE){ // si on est a la fin du buffer
			// remplir buffer
			errno = 0;
			while((n = read(f->fd, &f->buffer, BUFFER_SIZE)) == 0 && errno != 0){
				errno = 0;
			}

			f->indice_buffer = 0;
			f->position += n;
			f->nbRestant_buffer = n;
		} 

		if(f->nbRestant_buffer <= 0){ // si on est a la fin du fichier
			break;
		} else { // on ecrit dans le tableau utilisateur

			p[nbLu] = f->buffer[f->indice_buffer];
			nbLu++;
			f->indice_buffer++;
		}

	}

	return nbLu; // renvoie du nombre de caractère lus


}


int ecrire(const void *p, unsigned int taille, unsigned int nbelem, FICHIER *f){
	return -1;
}	