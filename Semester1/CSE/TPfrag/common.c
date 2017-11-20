#include "mem.h"
#include "common.h"
#include <assert.h>
#include <stdio.h>

#ifndef MEMORY_SIZE
#define MEMORY_SIZE 8192
#endif

static char memory[MEMORY_SIZE];

void *get_memory_adr() {
	return memory;
}

size_t get_memory_size() {
	return MEMORY_SIZE;
}

void *alloc_max(size_t estimate) {
	void *result;
	static size_t last = 0;

	while ((result = mem_alloc(estimate)) == NULL) {
		estimate--;
	}
	debug("Alloced %zu bytes at %p\n", estimate, result);
	if (last) {
		// Idempotence test
		assert(estimate == last);
	} else
		last = estimate;
	return result;
}
