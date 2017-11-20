#include "mem.h"
#include "common.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <assert.h>

#define MAX_ALLOC (1<<10)
#define NB_ALLOCS 7
#define NB_TESTS 6

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

static void partial_frees(void **ptr, int i) {
	if (i%3 == 0) {
		debug("Fisrt free\n");
		my_free(&ptr[4]);
		my_free(&ptr[6]);
	}
	my_free(&ptr[0]);
	my_free(&ptr[1]);
	if (i%3 == 1) {
		debug("Intermediate free\n");
		my_free(&ptr[4]);
		my_free(&ptr[6]);
	}
	my_free(&ptr[2]);
	if (i%3 == 2) {
		debug("Last free\n");
		my_free(&ptr[4]);
		my_free(&ptr[6]);
	}
}

void random_fill(unsigned char *m, size_t size) {
	for (size_t i=0; i<size; i++)
		m[i] = (unsigned char) random();
}

int main(int argc, char *argv[]) {
	void *ptr[NB_ALLOCS+1];
	unsigned char buffer[2*MAX_ALLOC];

	mem_init(get_memory_adr(), get_memory_size());
	fprintf(stderr, "Test réalisant divers cas de reallocs en faisant varier la possibilité\n"
			"d'extension du bloc et le moment de libération des blocs."
 		"\n");
	srandom(getpid());
	for (int i=0; i<NB_TESTS; i++) {
		void *new_ptr;

		allocs(ptr);
		random_fill(ptr[5], MAX_ALLOC);
		memcpy(buffer, ptr[5], MAX_ALLOC);
		partial_frees(ptr, i);
		debug("Extending realloc\n");
		new_ptr = mem_realloc(ptr[5], 2*MAX_ALLOC);
		assert(new_ptr == ptr[5]);
		assert(memcmp(buffer, new_ptr, MAX_ALLOC) == 0);
		random_fill(ptr[5], 2*MAX_ALLOC);
		memcpy(buffer, ptr[5], 2*MAX_ALLOC);
		debug("Moving realloc\n");
		new_ptr = mem_realloc(ptr[5], 3*MAX_ALLOC);
		assert((new_ptr != NULL) && (new_ptr != ptr[5]));
		assert(memcmp(buffer, new_ptr, 2*MAX_ALLOC) == 0);
		ptr[5] = new_ptr;
		debug("Failing realloc\n");
		new_ptr = mem_realloc(new_ptr, 4*MAX_ALLOC);
		assert(new_ptr == NULL);
		frees(ptr);
	}

	// TEST OK
	return 0;
}
