#include "mem.h"
#include "common.h"
#include <assert.h>
#include <stddef.h>
#include <pthread.h>
#include <time.h>
#include <stdint.h>
#include <string.h>
#include <sys/time.h>
#include <stdio.h>


#define BILLION 1000000000L
static pthread_mutex_t verrou = PTHREAD_MUTEX_INITIALIZER;

// constante définie dans gcc seulement
#ifdef __BIGGEST_ALIGNMENT__
#define ALIGNMENT __BIGGEST_ALIGNMENT__
#else
#define ALIGNMENT 16UL
#endif
static __thread int ain_lib=0;
#define dprintf(args...) \
  do { \
    if (!ain_lib) { \
      ain_lib=1; \
      printf(args); \
      ain_lib=0; \
    } \
  } while(0)


/* struct fb : décrit une zone de mémoire libre.
   La mémoire libre commence là où est placé la structure.
   Le champ 'taille' contient la taille totale de la zone libre
   en comptabilisant la taille de la structure 'struct fb'

   Le champ 'next' contient l'adresse de la prochaine zone libre dans
   l'ordre croissant de la mémoire (NULL si c'est la dernière).
   Deux zones libres ne peuvent pas être adjacentes (elles sont
   fusionnées en une seule au besoin)
*/

struct fb {
	size_t taille;
	struct fb *next;
};

/* struct db : décrit une zone de mémoire occupée.
   La zone allouée commence après cette structure de contrôle
   Le champ 'taille' contient la taille totale de cette zone
   en comptabilisant la taille de la structure 'struct db'


*/

struct db {
	size_t taille;
};

struct header {
	struct fb sentinel;
	mem_fit_function_t *fit;
};

static struct header *header() {
	return (struct header *) get_memory_adr();
}

static struct fb *sentinel() {
	return &header()->sentinel;
}

static void *first_bloc() {
	return header() + 1;
}

void mem_init(void* mem, size_t taille)
{
    pthread_mutex_lock (&verrou);
	/* Interface obsolète remplacée par common.c */
	assert(mem == get_memory_adr());
	assert(taille == get_memory_size());

	/* On initialise le premier bloc libre */
	struct fb *first_fb = first_bloc();
	first_fb->taille = get_memory_size() - sizeof(struct header);
	first_fb->next = NULL;

	/* On initialise l'entête */
	sentinel()->taille = 0;
	sentinel()->next = first_fb;
	
	/* Par défaut: fit first */
	mem_fit(&first_fit);
    //best_fit
    //worst_fit
    //first_fit
    pthread_mutex_unlock (&verrou);
}

void mem_fit(mem_fit_function_t *f)
{
	header()->fit = f;
}

void* mem_alloc(size_t taille)
{
    pthread_mutex_lock (&verrou);
    size_t usertaille = taille;
    /**********************************/
    //struct timeval start, end;
    //gettimeofday(&start, NULL);
    uint64_t diff;
    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC, &start);
    /**********************************/

	/* Mise à jour de la taille */
	/* On doit aussi stocker le descripteur */
	taille += sizeof(struct db);
	/* La zone doit au moins pouvoir stocker un 'struct fb'
	   (pour ne pas avoir de problème lors du free)
	*/
	if (taille < sizeof (struct fb)) {
		taille = sizeof(struct fb);
	}

	/* On aligne la taille sur le premier multiple d'un long (pour
	   que les structures restent alignées
	*/
	taille=(taille + (ALIGNMENT - 1)) & ~(ALIGNMENT - 1);

	/* On cherche le prédécesseur d'un bloc libre assez grand */
	/* Il existe toujours (si le bloc existe) grâce à la sentinelle */
	struct fb *prev = header()->fit(sentinel(), taille);
	
	if (!prev) {
		/* Rien de libre */
		return NULL;
	}

	/* On a trouvé le prédécesseur d'un block libre assez grand */
	/* On calcul de résidu */
	struct fb *current = prev->next;
	struct db *new_db = (struct db *) current;
	size_t remaining = current->taille - taille;

	/* S'il reste assez pour un bloc libre ... */
	/* ... on le crée */
	struct fb *new_fb = current->next;
	if (remaining  >= sizeof(struct fb)) {
		new_fb = (struct fb *) ((char *) new_db + taille);
		new_fb->taille = remaining;
		new_db->taille -= remaining;
		new_fb->next = current->next;
	}
	/* On met à jour la liste des blocs libres */
	prev->next = new_fb;
    pthread_mutex_unlock (&verrou);

    /**********************************/
    clock_gettime(CLOCK_MONOTONIC, &end);
    diff = BILLION * (end.tv_sec - start.tv_sec) + end.tv_nsec - start.tv_nsec;
	dprintf("mem_alloc@%lu;%lu;%llu@\n",(unsigned long) usertaille,(unsigned long) taille,(long long unsigned int) diff);//nanosec
    //gettimeofday(&end, NULL);
    //dprintf("__%ld__", ((end.tv_sec * 1000000 + end.tv_usec) - (start.tv_sec * 1000000 + start.tv_usec)));
    /**********************************/
	return new_db + 1;
}

/* Fusionne deux blocs s'ils sont consécutifs */
/* Met à jour taille et chaînage */
/* Renvoie le premier bloc si la fusion s'est faite, le second sinon */
static struct fb *merge_fbs(struct fb *prev, struct fb *current)
{
	/* Si les blocs sont consécutifs */
	if ((char *) prev + prev->taille == (char *) current) {
		/* On fusionne */
		prev->taille += current->taille;
		prev->next = current->next;
		return prev;
	} else {
		return current;
	}
}

static struct db *get_db(void *adr) {
	return (struct db *) adr - 1;
}

void mem_free(void *mem)
{
	if (mem == NULL) {
		return;
	}
    /**********************************
    uint64_t diff;
    struct timespec start, end;
    clock_gettime(CLOCK_MONOTONIC, &start);
    **********************************/

    pthread_mutex_lock (&verrou);
	/* On retrouve l'adresse du descripteur placé avant les données */
	struct db *db = get_db(mem);

	/* Quelques vérifications */
	assert((void *) db >= get_memory_adr());
	assert((char *) db + db->taille <= (char *) get_memory_adr()+get_memory_size());

	/* On crée un nouveau bloc libre et on le place dans la liste à la bonne position */
	struct fb *fb = (struct fb *) db;
	struct fb *prev = sentinel();
	struct fb *current = prev->next;
	while ((current != NULL) && (current < fb)) {
		prev = current;
		current = current->next;
	}
	prev->next = fb;
	fb->next = current;

	/* On fusionne au besoin le nouveau bloc créé */
	/* A gauche */
	fb = merge_fbs(prev, fb);
	/* A droite */
	merge_fbs(fb, current);
    pthread_mutex_unlock (&verrou);

    /**********************************
    clock_gettime(CLOCK_MONOTONIC, &end);
    diff = BILLION * (end.tv_sec - start.tv_sec) + end.tv_nsec - start.tv_nsec;
	dprintf("mem_free@%lu;%llu@\n",(unsigned long) db->taille,(long long unsigned int) diff);//nanosec
    **********************************/
}

void mem_show(void (*print)(void *, size_t, int))
{
	/* On suit les blocs libres... */
	struct fb *fb = sentinel()->next;
	/* ... et en même temps on parcours la mémoire séquentiellement */
	struct db *current = first_bloc();
	void *mem_end = get_memory_adr() + get_memory_size();

	while((void *) current < mem_end) {
		/* Si le bloc courant est libre, on avance dans la liste des libres */
		int free = 0;
		if (current == (struct db *) fb) {
			free = 1;
			fb = fb->next;
		}
		print(current+1, current->taille, free);
		current = (struct db *) ((char *) current + current->taille);
	}
}

struct fb *first_fit(struct fb* fb, size_t taille)
{
	/* On parcourt la liste jusqu'à la fin ou au premier bloc assez grand */
	while (fb->next != NULL) {
		if (fb->next->taille >= taille) {
			return fb;
		}
		fb=fb->next;
	}
	return NULL;
}

struct fb *best_fit(struct fb* fb, size_t taille)
{
	struct fb *best = NULL;

	while (fb->next != NULL) {
		if (fb->next->taille >= taille) {
			if ((best == NULL) || (best->next->taille > fb->next->taille))
				best = fb;
		}
		fb=fb->next;
	}
	return best;
}

struct fb *worst_fit(struct fb* fb, size_t taille)
{
	struct fb *worst = NULL;

	while (fb->next != NULL) {
		if (fb->next->taille >= taille) {
			if ((worst == NULL) || (worst->next->taille < fb->next->taille))
				worst = fb;
		}
		fb=fb->next;
	}
	return worst;
}

size_t mem_get_size(void* mem)
{
	if (mem == NULL) {
		return 0;
	}

	/* On retrouve l'adresse du descripteur placé avant les données */
	struct db *db = get_db(mem);
	return db->taille;
}

