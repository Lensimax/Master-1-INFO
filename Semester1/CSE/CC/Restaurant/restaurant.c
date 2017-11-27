#include "restaurant.h"

Restaurant restau;

void ouverture(){
	// init des thread
	// init des serveurs
}

void fermeture(){
	// destroy des thread
}

int service_en_cours(){
	return restau.service_cours;
}

// arrivé du client
void client_arrival_simulation(){

}


///// CLIENT ////////
// synchro

// attente d'un serveur
int client_get_waiter(int id_client, kind_task_t task){
	pthread_mutex_lock(&restau->m);

	while(restau.arrayClient[id_client] == -1){
		pthread_cond_wait(&restau->clients, &restau->m);
	}

	for(int i=0; i<nbServeurs; i++){
		if() // TODO
	}

	pthread_mutex_unlock(&restau->m);
}

// on relache le serveur
int client_release_waiter(int id_client, int serveur, kind_task_t task){
	pthread_mutex_lock(&restau->m);




	pthread_mutex_unlock(&restau->m);
}


// ACTION

int client_get_table(int id_client, int serveur){

}

void client_give_order(int id_client, int serveur, kind_task_t task){

}

void client_paie_and_leave(int id_client, int serveur, kind_task_t task){

}

///// WAITER ////////
// synchro

task_t waiter_get_task(int id_serveur){
	pthread_mutex_lock(&restau->m);




	pthread_mutex_unlock(&restau->m);
}

void waiter_task_done(int id_serveur, task_t task){
	pthread_mutex_lock(&restau->m);




	pthread_mutex_unlock(&restau->m);
}

// ACTION

void waiter_do_installation(int id_serveur, task_t task){

}

void waiter_do_order(int id_serveur, task_t task){

}

void waiter_do_bill(int id_serveur, task_t task){

}

