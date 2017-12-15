#include "stdes.h"
#include <stdarg.h>
#include <stdio.h>


int mypow(int x, int n){
	int res=1;
	while(n>0){
		n--;
		res *= x;
	}
	return res;
}

FICHIER *ouvrir(char *nom, char mode){
	FICHIER *file;
	//struct stat *infos;

	file = malloc(sizeof(FICHIER));

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
	file->nbRestant_buffer = 0;

	file->buffer = malloc(BUFFER_SIZE);

	return file;

}


int fermer(FICHIER*f){
	free(f->buffer);
	free(f);
	return close(f->fd);
}



int lire(void *p, unsigned int taille, unsigned int nbelem, FICHIER *f){
	
	int nbOctetsLus = 0;
	int n;
	int octetsALire = taille * nbelem;

	if(taille < 1 || nbelem < 1){
		return -1;
	}

	while(octetsALire - nbOctetsLus > 0){ // tant qu'il reste des choses à lire

		if(f->nbRestant_buffer == 0){ // le tampon est vide
			// remplir tampon
			errno = 0;
			while((n = read(f->fd, (void *)f->buffer, BUFFER_SIZE)) == 0 && errno != 0){
				errno = 0;
			}

			f->indice_buffer = 0;
			//f->position += n;
			f->nbRestant_buffer = n;
		} 

		if(f->nbRestant_buffer <= 0){ // si on est a la fin du fichier

			break;

		} else { // on ecrit dans le tableau utilisateur

			for(; f->nbRestant_buffer>0 && octetsALire>0 ; f->nbRestant_buffer--){

				((char*)p)[nbOctetsLus] = f->buffer[f->indice_buffer];
				nbOctetsLus++;
				f->indice_buffer ++;
				octetsALire--;

			}

		}

	}

	return nbOctetsLus/taille; // renvoie du nombre d' elements lus


}


int ecrire(const void *p, unsigned int taille, unsigned int nbelem, FICHIER *f){

	int nbOctetsEcrits = 0;
	int octetsAEcrire = taille * nbelem;

	if(taille < 1 || nbelem < 1){
		return -1;
	}

	while(octetsAEcrire - nbOctetsEcrits > 0){ // tant qu'il reste des choses à écrire

		if(f->nbRestant_buffer == 0){ // le tampon est vide
			// remplir tampon
			while(nbOctetsEcrits<octetsAEcrire && f->nbRestant_buffer<=BUFFER_SIZE){
				f->buffer[f->nbRestant_buffer] = ((char*)p)[nbOctetsEcrits];
				nbOctetsEcrits ++;
				f->nbRestant_buffer ++;
			}

			
		} 

		if(f->nbRestant_buffer <= 0){ // si on est a la fin du fichier

			break;

		} else { // on ecrit dans le fichier

			errno = 0;
			while((write(f->fd, (void *)f->buffer, f->nbRestant_buffer)) == 0 && errno != 0){
				errno = 0;
			}
			f->nbRestant_buffer = 0;

		}

	}

	return nbOctetsEcrits/taille;
}	


// si l'utilisateur rentre un chaine trop grande par rapport a la taille qu'il a alloué our ça chaine
// il y a une erreur de segmentation (comme dans le vrai scanf("%s",s))

int fliref(FICHIER *fp, char*format, ...){ // reprise du code du man.
	va_list ap;
	int *d;
	char *c, *s;
	char v;
	int i, somme;
	int dejaLuCaractere = 0; // dejaLuCaractere>0 <=> on a deja lu un caractere sans l'"utiliser"
	int nbPatternsReconnus = 0;

	va_start(ap, format);

	while (*format){
		switch (*format) {
			case '%':
				format ++;
				switch (*format) {
					case 's':              /* string sans espace*/
						s = (va_arg(ap, char *));
						if (!dejaLuCaractere){
							if(!lire(&v, 1, 1, fp)) return nbPatternsReconnus; //on lit, si il n'y a plus rien à lire on renvoie
						}
						else{
							dejaLuCaractere--;
						}
						i=0;
						while(v != ' '){
							s[i]=v;
							i++;
							if(!lire(&v, 1, 1, fp)) return nbPatternsReconnus;
						}
						dejaLuCaractere++; //on a lu un caractere en plus qu'on a pas utilisé
						nbPatternsReconnus++;
						break;
					case 'd':              /* int */
						d = (int *) (va_arg(ap, char *));
						i=0;
						somme=0;
						if (!dejaLuCaractere){
							if(!lire(&v, 1, 1, fp)) return nbPatternsReconnus;
						}
						else{
							dejaLuCaractere--;
						}
						while(v >= '0' && v <= '9'){
							somme = somme * mypow(10,i) + (v-48);
							i++;
							if(!lire(&v, 1, 1, fp)) return nbPatternsReconnus;
						}
						*d = somme;
						dejaLuCaractere++; //on a lu un caractere en plus qu'on a pas utilisé
						nbPatternsReconnus++;
						break;
					case 'c':              /* char */
						/* need a cast here since va_arg only
						takes fully promoted types */
						c = (va_arg(ap, char *));
						if (!dejaLuCaractere){
							if(!lire(&v, 1, 1, fp)) return nbPatternsReconnus;
						}
						else{
							dejaLuCaractere--;
						}
						*c = v;
						nbPatternsReconnus++;
					break;
				}
				//printf("pourcent");
				break;
			default:
				// printf("%c",*format);
				if (!dejaLuCaractere){
					if(!lire(&v, 1, 1, fp)) return nbPatternsReconnus;
				}
				else{
					dejaLuCaractere--;
				}
				if (v != *format)
					return nbPatternsReconnus;//TODOOOOOOOOOOOOOOOOOOOOOOOOO
				break;
		}
		format++;
	}

	va_end(ap);
	return nbPatternsReconnus;
}


int fecriref(FICHIER *fp, char*format, ...){ // reprise du code du man.
	va_list ap;
	int d;
	char c, *s;
	int nbCharAffiches = 0;

	va_start(ap, format);

	while (*format){
		switch (*format) {
			case '%':
				format ++;
				switch (*format) {
					case 's':              /* string */
						s = va_arg(ap, char *);
						while(*s){
							ecrire(s, 1, 1, fp);
							s++;
							nbCharAffiches++;
						}
						// printf("%%string %s%%", s);
						break;
					case 'd':              /* int */
						d = va_arg(ap, int);
						// printf("%%int %d%%", d);
						char str[BUFFER_SIZE];
						int length = sprintf(str, "%d", d); //  convertion int to String 
						ecrire(&str, 1, length, fp);
						nbCharAffiches += length;
						break;
					case 'c':              /* char */
						/* need a cast here since va_arg only
						takes fully promoted types */
						c = (char) va_arg(ap, int);
						// printf("%%char %c%%", c);
						ecrire(&c, 1, 1, fp);
						nbCharAffiches++;
					break;
				}
				//printf("pourcent");
				break;
			default:
				// printf("%c",*format);
				ecrire(format, 1, 1, fp);
				nbCharAffiches++;
				break;
		}
		format++;
	}

	va_end(ap);
	return nbCharAffiches;
}