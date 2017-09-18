#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <time.h>

#define NMAX 1024

#define N 3


int main(int argc, char **argv){

	int i;


	// pid_t initial_proc = getpid();

	int tube[2];

	long int number;


	for(i=0; i<N; i++){

		if(pipe(tube) == -1){
			perror("Pipe");
			exit(1);
		}		

		switch(fork()){
			case -1: 
				perror(NULL);
				exit(1);
				
			case 0: // fils
				close(tube[1]);

				read(tube[0], &number, sizeof(long int));

				printf("valeur recue %li pid %d\n", number, getpid());

				close(tube[0]);
				break;
			default:
				close(tube[0]);
				srandom (getpid());
				number = random()%2000;
				write(tube[1], &number, sizeof(long int));
				printf("valeur envoyee %li pid %d\n", number, getpid());
				close(tube[1]);
				exit(0);
		}

	}


	exit(0);
	

}