#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include <sys/types.h>
#include <unistd.h>
#include <stdlib.h>



int *tab;
int taille;
struct section_recherche *tab_s;

struct section_recherche {
	int to_find;
	int begin; 
	int end;
};

void lire(){
	int i;

	printf("Entrer taille: ");
	scanf(" %d", &taille);

	tab = malloc(taille * sizeof(int));

	for(i=0; i<taille; i++){
		scanf(" %d", &tab[i]);
	}
}

int search(void *str){

	struct section_recherche *section = (struct section_recherche *) str;

	printf("To Find %d Debut %d Fin %d\n", section->to_find, section->begin, section->end);


	return NULL;
}


int main(int argc, char **argv){

	int i;
	int nb_thread;
	pthread_t *tids;
	int nb_to_find;


	if(argc == 3){
		nb_thread = atoi(argv[1]);
		tids = malloc(nb_thread * sizeof(pthread_t));
		tab_s = malloc(nb_thread * sizeof(struct section_recherche));
		nb_to_find = atoi(argv[2]);
	} else {
		fprintf(stderr, "donner un nombre de thread et un nombre a chercher\n");
		exit(1);
	}

	lire();

	// affichage vecteur
	printf("{ ");
	for(i=0; i<taille; i++){
		printf("%d, ", tab[i]);
	}
	printf("}\n");


	int taille_section = taille / nb_thread; // taille max de vecteur dans lequel nous allons chercher 


	for(i=0; i<nb_thread; i++){

		tab_s[i].to_find = nb_to_find;

		if(i == nb_thread-1){ // dernier cas
			
			tab_s[i].begin = taille%nb_thread;
			tab_s[i].end = taille - 1;
		} else {
			tab_s[i].begin = i * taille_section;
			tab_s[i].end = (i+1) * taille_section;
		}


		pthread_create(&tids[i], NULL, (void * (*)(void *))search, &tab_s[i]);
	}


	for(i=0; i<nb_thread; i++){
		pthread_join(tids[i], NULL);
	}

	free(tids);

	exit(0);

}