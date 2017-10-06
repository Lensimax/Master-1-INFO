#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include <sys/types.h>
#include <unistd.h>
#include <stdlib.h>

char * french_chant = "Allons enfants de la patrie";
char * english_chant = "Swing low, sweet chariot";

struct s {
  char *song;
  int nb_sing;
};

void *supporter(void *arg){
  struct s *str = (struct s*) arg;
  int i;
  int pid;
  pthread_t tid;  
  pid = getpid();
  tid = pthread_self();
  srand((int)tid);


  for(i = 0; i < str->nb_sing; i++){
    printf("Processus %d Thread %x : %s \n", pid,(unsigned int)tid, str->song);
    usleep(rand()/ RAND_MAX * 1000000.);
  }
  return(void *)tid;
}

int main(int argc, char **argv){

  int team1;
  int team2;
  int i;
  int nb_threads = 0;
  pthread_t *tids;

  if(argc != 3){
    fprintf(stderr, "usage : %s team1 team2\n", argv[0]);
    exit(-1);
  }

  team1 = atoi(argv[1]);
  team2  = atoi(argv[2]);
  nb_threads = team1 + team2; 
  tids = malloc(nb_threads*sizeof(pthread_t));

  struct s french;
  french.song = french_chant;
  french.nb_sing = 6;

  struct s english;
  english.song = english_chant;
  english.nb_sing = 3;

  /* Create the threads for team1 */
  for(i = 0; i < team1; i++){
    pthread_create(&tids[i], NULL, supporter, &french);
  }
  /* Create the other threads(ie. team2)*/
  for( ; i < nb_threads; i++){
    pthread_create(&tids[i], NULL, supporter, &english);
  }  

  /* Wait until every thread ened */ 
  for(i = 0; i < nb_threads; i++){
    pthread_join(tids[i], NULL);
  }
  
  free(tids);
  return EXIT_SUCCESS;
}
