#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <math.h>
#include <sys/time.h>

#include "algo_principal.h"
#include "appels_sequentiels.h"

void *comportement_thread(void *arg) {
    traitement((arguments_t *) arg);
    pthread_exit(NULL);
}

void algo_principal(int parallelism, int *tableau, int taille, char *arg)
{
    arguments_t *arguments;
    pthread_t *tids;
    int inf, sup;
    int erreur, i;
    void *status, *args_algo;

    args_algo = initialisation(parallelism, tableau, taille, arg);

    tids = malloc(parallelism*sizeof(pthread_t));
    arguments = malloc(parallelism*sizeof(arguments_t));
    if ((tids == NULL) || (arguments == NULL)) {
        fprintf(stderr, "Impossible d'allouer les structures de gestion des "
                        "threads\n");
        exit(1);
    }

    inf = 0;
    sup = (taille-1) / parallelism;

    // debut capture temps
    struct timeval timedebut;
    if(gettimeofday(&timedebut, NULL) < 0){
        fprintf(stderr, "Erreur temps\n");
    }


    for (i=0; i<parallelism; i++) {
        arguments[i].num = i;
        arguments[i].inf = inf;
        arguments[i].sup = sup;
        arguments[i].tableau = tableau;
        arguments[i].taille = taille;
        arguments[i].args_algo = args_algo;

        erreur = pthread_create(&tids[i], NULL, comportement_thread,
                                &arguments[i]);
        if (erreur != 0) {
            fprintf(stderr, "Erreur de creation du thread %d\n", i);
            exit(1);
        }
        inf = sup+1;
        sup = ((i+2)*(taille-1)) / parallelism;
    }

    // debut capture temps
    struct timeval time_creation_thread;
    if(gettimeofday(&time_creation_thread, NULL) < 0){
        fprintf(stderr, "Erreur temps\n");
    }

    int temps_creation_thread = (time_creation_thread.tv_sec - timedebut.tv_sec) * pow(10, 6) + (time_creation_thread.tv_usec - timedebut.tv_usec);

    // printf("temps_creation_thread %d microsecondes\n", temps_creation_thread);



    for (i=0; i<parallelism; i++)
         pthread_join(tids[i], &status);

    traitement_resultats(parallelism, arguments);

    // fin capture temps
    struct timeval timefin;
    if(gettimeofday(&timefin, NULL) < 0){
        fprintf(stderr, "Erreur temps\n");
    }

    int secondes = timefin.tv_sec - timedebut.tv_sec;
    int usecondes = timefin.tv_usec - timedebut.tv_usec;

    int temps = secondes * pow(10, 6) + usecondes;

    printf("%d", temps);

    free(arguments);
    free(tids);
}
