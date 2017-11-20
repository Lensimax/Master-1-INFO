#ifndef __MEM_H
#define __MEM_H
#include <stddef.h>

struct fb;

void mem_init(void* mem, size_t taille);
void* mem_alloc(size_t);
void mem_free(void*);
size_t mem_get_size(void *);

/* Itérateur sur le contenu de l'allocateur */
void mem_show(void (*print)(void *, size_t, int free));

/* Choix de la strategie et strategies usuelles */
typedef struct fb* (mem_fit_function_t)(struct fb *, size_t);

void mem_fit(mem_fit_function_t*);
struct fb* first_fit(struct fb*, size_t);

/* Si vous avez le temps */
struct fb* best_fit(struct fb*, size_t);
struct fb* worst_fit(struct fb*, size_t);

#endif
