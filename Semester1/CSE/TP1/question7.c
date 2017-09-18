#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <time.h>

#define NMAX 1024

// #define N 4

long int max(int a, int b){
	if(a > b){
		return a;
	} else {
		return b;
	}
}

typedef struct {
	int num;
	long int val;
} Node;

	
				

int main(int argc, char **argv){

	int i;
	int N;

	if(argc != 2){
		exit(1);
	} else {
		N = atoi(argv[1]);
	}

	Node noeud;
	noeud.val = -1;
	noeud.num = -1;

	// int tube_aller[2];
	int tube_retour[2];
	int tube_aller[2];

	if(pipe(tube_retour) == -1){
		perror("Pipe");
		exit(1);
	}		

	long int number_rand;

	for(i=0; i<N; i++){


		if(pipe(tube_aller) == -1){
			perror("Pipe");
			exit(1);
		}
		
		switch(fork()){
			case -1: 
				perror(NULL);
				exit(1);
				
			case 0: // fils

				if(read(tube_aller[0], &noeud, sizeof(Node)) == -1){
					perror("Read");
					exit(1);
				}


				break;
			default:

				// calcul max + tirage

				srandom (getpid());
				number_rand = random() % 2000;

				int indice_node;
				long int tmp;

				// long int tmp = max(noeud.val, number_rand);
				if(noeud.val > number_rand){
					tmp = noeud.val;
					indice_node = noeud.num;
				} else {
					tmp = number_rand;
					indice_node = i;
				}
				// long int tmp = number_rand;

				noeud.val = number_rand;
				noeud.num = i;

				Node tmp_noeud;
				tmp_noeud.val = tmp;
				tmp_noeud.num = indice_node;

				printf("processus pid %d node %d val = %li\n", getpid(), noeud.num, noeud.val);

				if(i == 0){ // père initial

					

					if(write(tube_aller[1], &tmp_noeud, sizeof(Node)) == -1){
						perror("Write");
						exit(1);
					}

					// attente du maximum

					read(tube_retour[0], &noeud, sizeof(Node));

					printf("\nLe maximum est %li du noeud %d !!!\n", noeud.val, noeud.num);

					exit(0);

				} else if(i == N-1){ // dernier fils

					write(tube_retour[1], &tmp_noeud, sizeof(Node));					

					exit(0);

				} else { // les autres processus


					if(write(tube_aller[1], &tmp_noeud, sizeof(Node)) == -1){
						perror("Write");
						exit(1);
					}
					exit(0);
				}

		}

	}


	exit(0);
	

}