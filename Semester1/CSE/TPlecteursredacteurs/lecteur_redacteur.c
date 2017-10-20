#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include "lecteur_redacteur.h"
#include <assert.h>

void initialiser_lecteur_redacteur(lecteur_redacteur_t *lr){
	pthread_mutex_init(&lr->m, NULL);
	lr->isWriting = 0;
	lr->nbLect = 0;
	pthread_cond_init(&lr->clect, NULL);
	pthread_cond_init(&lr->ccrire, NULL);
}

void debut_lecture(lecteur_redacteur_t *lr){
	pthread_mutex_lock(&lr->m);
	while(lr->isWriting){
		pthread_cond_wait(&lr->clect, &lr->m);
	}
	lr->nbLect++;
	pthread_mutex_unlock(&lr->m);
}

void fin_lecture(lecteur_redacteur_t *lr){
	pthread_mutex_lock(&lr->m);
	lr->nbLect--;
	assert(lr->nbLect >= 0);
	if(lr->nbLect == 0){
		pthread_cond_signal(&lr->ccrire);
	}
	pthread_mutex_unlock(&lr->m);
}

void debut_redaction(lecteur_redacteur_t *lr){
	pthread_mutex_lock(&lr->m);
	while(lr->isWriting || lr->nbLect){
		pthread_cond_wait(&lr->ccrire, &lr->m);
	}
	lr->isWriting = 1;
	pthread_mutex_unlock(&lr->m);
}

void fin_redaction(lecteur_redacteur_t *lr){
	pthread_mutex_lock(&lr->m);
	lr->isWriting = 0;
	pthread_cond_broadcast(&lr->ccrire);
	pthread_cond_broadcast(&lr->clect);
	pthread_mutex_unlock(&lr->m);
}

void detruire_lecteur_redacteur(lecteur_redacteur_t *lr){
	pthread_mutex_destroy(&lr->m);
	pthread_cond_destroy(&lr->ccrire);
	pthread_cond_destroy(&lr->clect);
}