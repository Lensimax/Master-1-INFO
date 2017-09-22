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

	// TODO !!!  faire des multiples de fb 
	void * adr_retour = (void *)(adr + adr->size - size_block); // valeur renvoyer par malloc
	adr->size = adr->size - size_block - sizeof(size_t);

	// affectation de la taille dans le block d'avant
	// *((size_t) adr_retour - 1) = size_block;

	return adr_retour;
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


	while(ptr != NULL && ptr->size > size_block + sizeof(size_t)){ 
		ptr = ptr->next;
	}

	if(ptr == NULL){
		printf("Pas de block trouvé\n");
		return NULL;
	} else {
		return ptr;
	}
}
