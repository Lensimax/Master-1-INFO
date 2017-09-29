#include "mem.h"
#include "common.h"
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#define NB_TESTS 10

void afficher_zone(void *adresse, size_t taille, int free)
{
  printf("Zone %s, Adresse : %lx, Taille : %lu\n", free?"libre":"occupee",
         (unsigned long) adresse, (unsigned long) taille);
}

void * alloc(size_t estimate) {
	void *result;

	if((result = mem_alloc(estimate)) == NULL) {
		exit(1);
	}
	debug("Alloced %zu bytes at %p\n", estimate, result);

	return result;
}

int main(int argc, char *argv[]) {

	mem_init(get_memory_adr(), get_memory_size());
	void * ptr1 = alloc(sizeof(int) * 10);
	void * ptr2 = alloc(sizeof(int) * 10);
	void * ptr3 = alloc(sizeof(int) * 10);


	mem_show(&afficher_zone);

	mem_free(ptr2);
	mem_free(ptr1);

	printf("Après free\n");
	mem_show(&afficher_zone);




	void * ptr4 = alloc(sizeof(int) * 10);

	printf("apres alloc\n");
	mem_show(&afficher_zone);


	// TEST OK
	return 0;
}
