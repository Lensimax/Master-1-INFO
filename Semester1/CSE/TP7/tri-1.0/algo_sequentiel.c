#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <math.h>
#include <sys/time.h>

#include "algo_principal.h"
#include "appels_sequentiels.h"

void algo_principal(int parallelism, int *tableau, int taille, char *arg)
{
    void *args_algo;
    arguments_t args;

    (void) parallelism;
    args_algo = initialisation(0, tableau, taille, arg);
    args.num = 0;
    args.inf = 0;
    args.sup = taille-1;
    args.tableau = tableau;
    args.taille = taille;
    args.args_algo = args_algo;

    struct timeval timedebut;
    if(gettimeofday(&timedebut, NULL) < 0){
        fprintf(stderr, "Erreur temps\n");
    }

    traitement(&args);

    struct timeval timefin;
    if(gettimeofday(&timefin, NULL) < 0){
        fprintf(stderr, "Erreur temps\n");
    }

    int secondes = timefin.tv_sec - timedebut.tv_sec;
    int usecondes = timefin.tv_usec - timedebut.tv_usec;

    int temps = secondes * pow(10, 6) + usecondes;

    printf("%d ", temps);


    traitement_resultats(0, &args); 
}
