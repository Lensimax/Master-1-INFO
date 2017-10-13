#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>

#define TAILLE_RIZ 50
#define BOUCHEE 10

int nb_philosophe;

pthread_mutex_t m;

int *baguette_dispo; // tableau boolean
int *bol_riz;
pthread_cond_t *cond_tab;
pthread_t *tids;

void synchro(int i){

	pthread_mutex_lock(&m);

	while(!baguette_dispo[i] || !baguette_dispo[(i+1)%nb_philosophe]){
		pthread_cond_wait(&cond_tab[i], &m);
		printf("[philosphe %d]: notifié\n", i);
	}

	baguette_dispo[i] = 0;
	baguette_dispo[(i+1)%nb_philosophe] = 0;

	pthread_mutex_unlock(&m);

}

void fin_synchro(int i){

	pthread_mutex_lock(&m);

	baguette_dispo[i] = 1;
	baguette_dispo[(i+1)%nb_philosophe] = 1;

	printf("[philosphe %d] précédent %d\n", i, (i-1+nb_philosophe)%nb_philosophe);
	printf("[philosphe %d] suivant %d\n", i,(i+1)%nb_philosophe);

	pthread_cond_signal(&cond_tab[(i-1+nb_philosophe)%nb_philosophe]);
	pthread_cond_signal(&cond_tab[(i+1)%nb_philosophe]);

	pthread_mutex_unlock(&m);	

}

void mange(int i){
	bol_riz[i] -= BOUCHEE;
	printf("[philosophe %d]: mange\n", i);
}

// executé par le thread
void *philosphe(void *arg){

	int *index = (int*)arg;

	while(1){

		synchro(*index);

		// section critique

		mange(*index);

		fin_synchro(*index);

		if(bol_riz[*index] <= 0){
			printf("[philosphe %d]: a fini de manger\n", *index);
			break;
		}
	}

	return NULL;
}


int main(int argc, char **argv){

	int i;

	if(argc != 2){
		fprintf(stderr, "Entrer le nombre de philosphe\n");
		exit(1);
	}

	nb_philosophe = atoi(argv[1]);

	// alloc dynamique

	baguette_dispo = malloc(sizeof(int) * nb_philosophe);
	bol_riz = malloc(sizeof(int) * nb_philosophe);
	cond_tab = malloc(sizeof(pthread_cond_t) * nb_philosophe);
	tids = malloc(sizeof(pthread_t) * nb_philosophe);

	// init tab

	pthread_mutex_init(&m, NULL);

	for(i=0; i<nb_philosophe; i++){
		baguette_dispo[i] = 1; // true
		bol_riz[i] = TAILLE_RIZ;
		pthread_cond_init(&cond_tab[i], NULL);

	}

	for(i=0; i<nb_philosophe; i++){
		pthread_create(&tids[i], NULL, philosphe, &i);
	}



	for(i=0; i<nb_philosophe; i++){
		pthread_join(tids[i], NULL);
	}


	free(baguette_dispo);
	free(bol_riz);
	free(cond_tab);
	free(tids);


	exit(0);
}


