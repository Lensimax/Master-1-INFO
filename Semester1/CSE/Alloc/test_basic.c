#include "mem.h"
#include "common.h"
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>

#define NB_TESTS 10

void alloc(size_t estimate) {
	void *result;

	if((result = mem_alloc(estimate)) == NULL) {
		exit(1);
	}
	debug("Alloced %zu bytes at %p\n", estimate, result);
}

int main(int argc, char *argv[]) {

	mem_init(get_memory_adr(), get_memory_size());
	alloc(sizeof(int) * 10);
	alloc(sizeof(int) * 50);
	alloc(sizeof(int) * 25);




	// TEST OK
	return 0;
}
