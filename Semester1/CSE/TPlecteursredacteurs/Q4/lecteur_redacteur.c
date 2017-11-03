#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include "lecteur_redacteur.h"
#include <assert.h>

//// STRUCTURE LISTE CHAINEE

#define REDACT 1
#define LECT 2

#define DEBUG 0

typedef struct cell cell;

struct cell {
	int type;
	pthread_cond_t cond;
	cell *next;
};


cell *head;
cell *tail;

// enfiler
pthread_cond_t * fifo_add(int type){

	cell *c = malloc(sizeof(cell));
	c->type = type;
	pthread_cond_init(&c->cond, NULL);
	c->next = NULL;

	if(tail != NULL){
		tail->next = c;
	}
	tail = c;

	if(head == NULL){
		head = c;
	}

	return &c->cond;
}

// renvoi le type de celui qui est en tete de file
int fifo_top_type(){
	if(head == NULL){
		if(DEBUG){
			printf("FIFO Vide\n");
		}
		return -1;
	} else {
		return head->type;
	}
}

// renvoi le cond_t de celui qui est en tete de file
pthread_cond_t* fifo_top_cond(){
	if(head == NULL){
		if(DEBUG){
			printf("FIFO Vide\n");
		}
		return NULL;
	} else {
		return &head->cond;
	}
}

// defiler
void fifo_delete(){
	cell *ptr = head;
	if(head == NULL){
		fprintf(stderr, "File vide\n");
	} else {
		head = head->next;
		pthread_cond_destroy(&ptr->cond);
		free(ptr);
	}

}


//// SYNCHRO


// reveil de thread en fonction de la fifo
void reveil(){

	if(fifo_top_type() != -1){ // la file est vide

		if(fifo_top_type() == LECT){ 

			if(DEBUG){
				printf("Reveil d'un LECT\n");
			}
			pthread_cond_signal(fifo_top_cond());
			fifo_delete();

			// on reveil tant que c'est pas un redacteur
			while(fifo_top_type() == LECT){
				if(DEBUG){
					printf("[BOUCLE]Reveil d'un LECT\n");
				}
				pthread_cond_signal(fifo_top_cond());
				fifo_delete();

			}
		} else if(fifo_top_type() == REDACT) {
			if(DEBUG){
				printf("Reveil d'un REDACT\n");
			}
			pthread_cond_signal(fifo_top_cond());
			fifo_delete();
		}
	} else {
		if(DEBUG){
			printf("File Vide\n");
		}
	}
}




void debut_lecture(lecteur_redacteur_t *lr){
	pthread_mutex_lock(&lr->m);

	pthread_cond_t *cond;
	
	if(head == NULL && !lr->isWriting && !lr->nbLect){ // personne dans la file et personne qui travaille
		if(DEBUG){
			printf("Thread %x Personne dans la file LECT\n", (int) pthread_self());
		}
	} else {
		if(DEBUG){
			printf("Thread %x Entree dans la file LECT\n", (int) pthread_self());
		}

		cond = fifo_add(LECT);

		pthread_cond_wait(cond, &lr->m);

	}
	lr->nbLect++;

	pthread_mutex_unlock(&lr->m);

}


void fin_lecture(lecteur_redacteur_t *lr){
	pthread_mutex_lock(&lr->m);
	if(DEBUG){
		printf("Fin LECT\n");
	}
	lr->nbLect--;

	if(lr->nbLect == 0){
		reveil();	
	}

	pthread_mutex_unlock(&lr->m);
}

void debut_redaction(lecteur_redacteur_t *lr){
	pthread_mutex_lock(&lr->m);

	pthread_cond_t *cond;


	if(head == NULL && !lr->isWriting && !lr->nbLect){

		if(DEBUG){
			printf("Thread %x Entree dans la file REDACT\n", (int) pthread_self());
		}
	} else {

		if(DEBUG){
			printf("Thread %x Entree dans la file REDACT\n", (int) pthread_self());
		}
		
		cond = fifo_add(REDACT);

		pthread_cond_wait(cond, &lr->m);
	}

	lr->isWriting = 1;
	pthread_mutex_unlock(&lr->m);
}

void fin_redaction(lecteur_redacteur_t *lr){
	pthread_mutex_lock(&lr->m);
	
	if(DEBUG){
		printf("Fin REDACT\n");
	}
	lr->isWriting = 0;
	reveil();
	
	pthread_mutex_unlock(&lr->m);
}


void initialiser_lecteur_redacteur(lecteur_redacteur_t *lr){
	pthread_mutex_init(&lr->m, NULL);
	lr->nbLect = 0; // nombre de lecteur qui lise
	lr->isWriting = 0; // si un redacteur écrit ou pas
	head = NULL;
	tail = NULL;
}


void detruire_lecteur_redacteur(lecteur_redacteur_t *lr){
	pthread_mutex_destroy(&lr->m);
}