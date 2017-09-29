#include "mem.h"
#include <stdio.h>

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

	void *adr_retour = (void *) adr + adr->size - taille_bloc;

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

	// TODO vérifier si adr_to_free entre la borne inf et sup

	void * adr_size = (size_t *) adr_to_free - 1;
	size_t size_free = *(size_t *) adr_size;

	// on recherche les endroits où il faut ajouter

	struct fb *ptr = head;
	struct fb *suiv = head;

	if(ptr != NULL){
		suiv = ptr->next;
	}

	// parcours de la liste chainée
	while(suiv != NULL && (void *) suiv > adr_to_free){
		ptr = suiv;
		suiv = suiv->next;
	}

	// on ajoute entre ptr et suiv
	struct fb *ptr_debut;

	// on crée notre fb
	struct fb zone_free;
	zone_free.size = size_free;
	zone_free.next = suiv;

	// on la stocke 
	*(struct fb *) adr_size = zone_free;

	// on chaine
	ptr_debut = adr_size;
	ptr->next = ptr_debut;


	if((long int) ptr + (long int) ptr->size == (long int) adr_size){ // la zone a libérée est adjacente a la zone libre précédente
		printf("fusion précédente");
		ptr_debut = ptr;
		ptr_debut->size = ptr_debut->size + size_free + sizeof(size_t); // on augmente la taille de la zone libre d'avant
		ptr->next = suiv;
	}

	if(suiv != NULL && (long int)adr_to_free + (long int)size_free == (long int) suiv - (long int)sizeof(size_t)){ // la zone a libere est adjacente a la zone libre suivante
		printf("fusion suivante");
		ptr_debut->size = ptr_debut->size + suiv->size + sizeof(size_t);
		ptr_debut->next = suiv->next;
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
