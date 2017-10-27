#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include "lecteur_redacteur.h"
#include <assert.h>

//// STRUCTURE LISTE CHAINEE

#define REDACT 1
#define LECT 2

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

int fifo_top_type(){
	if(head == NULL){
		printf("FIFO vide\n");
		return -1;
	} else {
		return head->type;
	}
}

pthread_cond_t* fifo_top_cond(){
	if(head == NULL){
		printf("FIFO vide\n");
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


// synchro


// reveil de thread en fonction de la fifo
void reveil(){

	if(fifo_top_type() != -1){

		if(fifo_top_type() == LECT){

			pthread_cond_signal(fifo_top_cond());
			fifo_delete();

			// on reveil tant que c'est pas un redacteur
			while(fifo_top_type() == LECT){
				pthread_cond_signal(fifo_top_cond());
				fifo_delete();

			}
		} else if(fifo_top_type() == REDACT) {
			pthread_cond_signal(fifo_top_cond());
			fifo_delete();
		}
	}
}


void initialiser_lecteur_redacteur(lecteur_redacteur_t *lr){
	pthread_mutex_init(&lr->m, NULL);
	head = NULL;
	tail = NULL;
}

void debut_lecture(lecteur_redacteur_t *lr){
	pthread_mutex_lock(&lr->m);


	//	si la file est vide et que quelqu'un travail il faut ajouter dans la liste chainée
	// si la vide est vide et que personne ne travaille il peut passer direct

	// if(head == NULL && ())
	
	pthread_cond_t *cond;

	
	cond = fifo_add(LECT);

	pthread_cond_wait(cond, &lr->m);

	pthread_mutex_unlock(&lr->m);
}


void fin_lecture(lecteur_redacteur_t *lr){
	pthread_mutex_lock(&lr->m);

	reveil();	

	pthread_mutex_unlock(&lr->m);
}

void debut_redaction(lecteur_redacteur_t *lr){
	pthread_mutex_lock(&lr->m);

	pthread_cond_t *cond;

	
	cond = fifo_add(REDACT);

	pthread_cond_wait(cond, &lr->m);

	pthread_mutex_unlock(&lr->m);
}

void fin_redaction(lecteur_redacteur_t *lr){
	pthread_mutex_lock(&lr->m);
	
	reveil();
	
	pthread_mutex_unlock(&lr->m);
}

void detruire_lecteur_redacteur(lecteur_redacteur_t *lr){
	pthread_mutex_destroy(&lr->m);
}