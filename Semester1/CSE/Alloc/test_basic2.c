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

	printf("\nPtr1 %p et debut de la zone %p\n", ptr1, ptr1 - sizeof(size_t));
	printf("Ptr2 %p et debut de la zone %p\n", ptr2, ptr2 - sizeof(size_t));
	printf("Ptr3 %p et debut de la zone %p\n\n", ptr3, ptr3 - sizeof(size_t));

	mem_show(&afficher_zone);

	printf("\nFree ptr2 %p\n", ptr2);
	mem_free(ptr2);
	mem_show(&afficher_zone);

	
	printf("\nFree ptr1 %p\n", ptr1);
	mem_free(ptr1);
	mem_show(&afficher_zone);

	
	return 0;
}
