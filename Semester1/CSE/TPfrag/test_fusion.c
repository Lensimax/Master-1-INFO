#include "mem.h"
#include "common.h"
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#define MAX_ALLOC (1<<10)
#define NB_TESTS 5

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

static void alloc5(void **ptr) {
	ptr[0] = checked_alloc(MAX_ALLOC);
	ptr[1] = checked_alloc(MAX_ALLOC);
	ptr[2] = checked_alloc(MAX_ALLOC);
	ptr[3] = checked_alloc(MAX_ALLOC);
	ptr[4] = alloc_max(get_memory_size() - 4*MAX_ALLOC);
}

static void free5(void **ptr) {
	for (int i=0; i<5; i++) {
		my_free(&ptr[i]);
	}
}

int main(int argc, char *argv[]) {
	void *ptr[5];

	mem_init(get_memory_adr(), get_memory_size());
	fprintf(stderr, "Test réalisant divers cas de fusion (avant, arrière et double\n"
			"Définir DEBUG à la compilation pour avoir une sortie un peu plus verbeuse."
 		"\n");
	for (int i=0; i<NB_TESTS; i++) {
		debug("Fusion avant\n");
		alloc5(ptr);
		my_free(&ptr[2]);
		my_free(&ptr[1]);
		ptr[1] = checked_alloc(2*MAX_ALLOC);
		free5(ptr);

		debug("Fusion arrière\n");
		alloc5(ptr);
		my_free(&ptr[1]);
		my_free(&ptr[2]);
		ptr[1] = checked_alloc(2*MAX_ALLOC);
		free5(ptr);

		debug("Fusion avant/arrière\n");
		alloc5(ptr);
		my_free(&ptr[1]);
		my_free(&ptr[3]);
		my_free(&ptr[2]);
		ptr[1] = checked_alloc(3*MAX_ALLOC);
		free5(ptr);
	}

	// TEST OK
	return 0;
}
