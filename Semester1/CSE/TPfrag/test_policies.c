#include "mem.h"
#include "common.h"
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#define MAX_ALLOC (1<<10)
#define NB_ALLOCS 8
#define NB_TESTS 4

void my_free(void **mem) {
	if (*mem != NULL) {
		mem_free(*mem);
		debug("Freed %p\n", *mem);
		*mem = NULL;
	}
}

static void *checked_alloc(size_t s) {
	void *result;

	assert((result = mem_alloc(s)) != NULL);
	debug("Alloced %zu bytes at %p\n", s, result);
	return result;
}

static void allocs(void **ptr) {
	int i;
	for (i=0; i<NB_ALLOCS; i++)
		ptr[i] = checked_alloc(MAX_ALLOC);
	ptr[i] = alloc_max(get_memory_size() - NB_ALLOCS*MAX_ALLOC);
}

static void frees(void **ptr) {
	for (int i=0; i<NB_ALLOCS+1; i++)
		my_free(&ptr[i]);
}

static void partial_frees(void **ptr) {
	debug("Cheese elaboration\n");
	my_free(&ptr[1]);
	my_free(&ptr[6]);
	my_free(&ptr[7]);
	my_free(&ptr[3]);
	my_free(&ptr[5]);
	my_free(&ptr[0]);
}

int ordered(void *a, void *b, void *c) {
	return (a < b && b < c) || (c < b && b < a);
}

int main(int argc, char *argv[]) {
	void *ptr[NB_ALLOCS+1];

	mem_init(get_memory_adr(), get_memory_size());
	fprintf(stderr, "Test mettant en place des trous de taille variable afin de\n"
			"le bon comportement des politiques d'allocation."
 		"\n");
	for (int i=0; i<NB_TESTS; i++) {
		allocs(ptr);
		partial_frees(ptr);
		if (i%2 == 0) {
			debug("Best fit\n");
			mem_fit(best_fit);
			ptr[3] = checked_alloc(MAX_ALLOC);
			assert(ordered(ptr[2], ptr[3], ptr[4]));
		} else {
			debug("Worst fit\n");
			mem_fit(worst_fit);
			ptr[5] = checked_alloc(MAX_ALLOC);
			assert(ordered(ptr[4], ptr[5], ptr[8]));
		}
		frees(ptr);
	}

	// TEST OK
	return 0;
}
