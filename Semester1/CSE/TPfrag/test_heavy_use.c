#include "mem.h"
#include "common.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#define MAX_ALLOC (1<<10)
#define MAX_BLOC (1<<9)
#define NB_TESTS 1000

#ifndef mem_realloc
#define mem_realloc(p,s) NULL
#endif

static void *allocs[MAX_ALLOC];
static size_t sizes[MAX_ALLOC];
static size_t total_size, max_total_size;
static size_t nb_alloc, max_nb_alloc;

int update(size_t *current, size_t increment, size_t *max) {
	*current += increment;
	if (*current > *max) {
		*max = *current;
		return 1;
	} else
		return 0;
}

void handle_new_alloc(unsigned char *result, size_t size, size_t location) {
	for (int j=0; j<size; j++)
		result[j] = (unsigned char) random();
	allocs[location] = result;
	sizes[location] = size;
	int updated = update(&nb_alloc, 1, &max_nb_alloc);
	updated |= update(&total_size, size, &max_total_size);
	if (updated)
		debug("Total size has grown to %zu in %zu blocs\n", total_size, nb_alloc);
}

int main(int argc, char *argv[]) {
	nb_alloc = 0;
	max_nb_alloc = 0;
	total_size = 0;
	max_total_size = 0;
	mem_init(get_memory_adr(), get_memory_size());
	fprintf(stderr, "Test réalisant une série aléatoire de tests. Chaque test consiste soit en :\n"
			"- une allocation, avec une chance de 0.5 si des blocs ont été alloués et s'il\n"
		        "  reste de la place. La mémoire ainsi allouée est remplie d'octets aléatoires\n"
			"- une libération, avec une chance de 0.5 si des blocs ont été alloués et s'il\n"
			"  de la place dans la table interne\n"
			"Définir DEBUG à la compilation pour avoir une sortie un peu plus verbeuse."
 		"\n");
	srandom(getpid());
	for (int i=0; i<NB_TESTS; i++) {
		long tirage = random();
		if (nb_alloc && ((tirage & 6) == 6)) {
			int j = random() % nb_alloc;
			size_t new_size = sizes[j] + (random() & (MAX_BLOC -1));
			unsigned char *result = mem_realloc(allocs[j], new_size);
			if (result) {
				total_size -= sizes[j];
				nb_alloc--;
				debug("Realloced from %zu bytes at %p to %zu bytes at %p\n", sizes[j],
						allocs[j], new_size, result);
				handle_new_alloc(result, new_size, j);
			} else {
				debug("*** FAILED to realloc from %zu bytes to %zu bytes at %p, "
					"got %zu bytes alloced in %zu blocs\n",
					sizes[j], new_size, allocs[j], total_size, nb_alloc);
			}
		} else if ((nb_alloc && (tirage & 1)) ||
		    		(nb_alloc == MAX_ALLOC)) {
			int j = random() % nb_alloc;
			mem_free(allocs[j]);
			total_size -= sizes[j];
			debug("Freed %zu bytes at %p\n", sizes[j], allocs[j]);
			nb_alloc--;
			allocs[j] = allocs[nb_alloc];
			sizes[j] = sizes[nb_alloc];
		} else {
			size_t size = random() & (MAX_BLOC-1);
			unsigned char *result = mem_alloc(size);
			if (result) {
				debug("Alloced %zu bytes at %p\n", size, result);
				handle_new_alloc(result, size, nb_alloc);
			} else {
				debug("*** FAILED to alloc %zu bytes, got %zu bytes alloced in %zu blocs\n",
						size, total_size, nb_alloc);
			}
		}
	}

	// TEST OK
	return 0;
}
