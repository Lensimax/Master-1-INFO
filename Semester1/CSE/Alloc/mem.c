#include "mem.h"
#include <stdio.h>
#include <stdlib.h>

struct fb {
	size_t size;
	struct fb *next;
};

struct fb *head; // tête de la liste chainée

void *borne_inf;
void *borne_sup;

mem_fit_function_t *fonction_recherche_block;



// creation d'une zone libre où l'on va allouer dynamiquement
void mem_init(char* mem, size_t taille){
	borne_inf = mem;
	borne_sup = mem + taille;

	head = (struct fb*) mem;

	// init premier elt de la liste chainée
	head->size = taille;
	head->next = NULL;

	mem_fit(mem_fit_first); // a changer selon la fonction de "fit"

}



void* mem_alloc(size_t size_block){
	struct fb *adr; // adresse où l'on veut alloué

	adr = fonction_recherche_block(head, size_block);

	if(adr == NULL){ // si on a pas trouvé de block on renvoi une adresse NULL
		return NULL;
	}

	int nb_block_fp = (size_block / sizeof(struct fb)) + 1;
	size_t taille_bloc = nb_block_fp * sizeof(struct fb);

	void *adr_retour = (char *) adr + adr->size - taille_bloc;

	// void *adr_retour = (void *)(adr + adr->size - taille_bloc); // valeur renvoyer par malloc
	
	adr->size = adr->size - taille_bloc - sizeof(size_t);

	// affectation de la taille dans le block d'avant
	void *adr_size = (size_t *) adr_retour - 1;
	*(size_t *) adr_size = taille_bloc;

	// TODO cas ou on alloue tous le bloc libre
	// il faut garder le pointeur vers le nouveau bloc libre

	return adr_retour;
}

/*struct fb* recherche_file_adr(void *adr){
	struct fb *ptr = head;


}*/


void mem_free(void *adr_to_free){

	if(adr_to_free > borne_sup || adr_to_free < borne_inf){
		fprintf(stderr, "Free en dehors des bornes (SEGFAULT)\n");
		exit(1);
	}

	void * adr_size = (size_t *) adr_to_free - 1;
	size_t size_free = *(size_t *) adr_size;

	// on recherche les endroits où il faut ajouter

	struct fb *ptr = head;
	struct fb *suiv = head;

	if(ptr != NULL){
		suiv = ptr->next;
	}


	// parcours de la liste chainée
	while(suiv != NULL && (char *) suiv < (char *) adr_to_free){
		printf("[BOUCLE] passe\n");
		ptr = suiv;
		suiv = suiv->next;
	}

	// sortie de la boucle de recherche d'emplacement

	// on ajoute entre ptr et suiv
	struct fb *ptr_debut;

	// on crée notre fb
	struct fb zone_free;
	zone_free.size = size_free + sizeof(size_t);
	zone_free.next = suiv;

	printf("size free: %lu\n", zone_free.size);

	// on la stocke 
	*(struct fb *) adr_size = zone_free;

	// puis on chaine
	ptr_debut = adr_size;
	ptr->next = ptr_debut;


	// fusion suivante
	if(suiv != NULL && (char *)ptr_debut + ptr_debut->size == (char *) suiv){ // la zone a libere est adjacente a la zone libre suivante

		ptr_debut->size = ptr_debut->size + suiv->size; // on augmente la taille de notre bloc courant
		ptr_debut->next = suiv->next;
	}

	// fusion précédente
	if((char *) ptr + ptr->size  == (char *) ptr_debut){ // la zone a libérée est adjacente a la zone libre précédente

		ptr->size = ptr->size + ptr_debut->size; // on augmente la taille de la zone libre d'avant
		ptr->next = ptr_debut->next; // on relie la zone d'avant avec la suite
	}


}


void mem_show(void (*print)(void *adr, size_t size, int free)){

	struct fb *ptr = head;

	while(ptr != NULL){
		print(ptr, ptr->size, 1);
		ptr = ptr->next;
	}
}


// renvoie la taille du block alloué a l'adresse adr
size_t mem_get_size(void *adr){
	return (size_t) adr - 1;
}



// affecte la fonction de "fit" a notre variable global fonction_recherche_block
void mem_fit(mem_fit_function_t *f){
	fonction_recherche_block = *f;
}


// retourne l'adresse du premier block ayant une taille suffisante, NULL si pas trouvé

struct fb* mem_fit_first(struct fb *f, size_t size_block){

	struct fb *ptr = f;

	int nb_block_fp = (size_block / sizeof(struct fb));
	size_t taille_bloc = nb_block_fp * sizeof(struct fb);

	while(ptr != NULL && ptr->size < taille_bloc + sizeof(size_t)){ 
		ptr = ptr->next;
	}

	if(ptr == NULL){
		return NULL;
	} else {
		return ptr;
	}
}
