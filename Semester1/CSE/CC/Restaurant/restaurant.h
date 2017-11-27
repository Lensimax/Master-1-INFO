#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define MAX_CLIENT 50
#define MAX_SERVEUR 50

typedef enum {
	INSTALLATION, ORDER, BILL
} kind_task_t;

typedef struct {
	kind_task_t kind;
	int client_id;
} task_t;

typedef struct {
	int arrayClient[MAX_CLIENT];
	int arrayServeur[MAX_SERVEUR]
	int service_cours;

	pthread_cond_t serveurs;
	pthread_cond_t clients;
	pthread_mutex_t m;
} Restaurant;

void ouverture();

void client(int id);
void waiter(int id);

void fermeture();