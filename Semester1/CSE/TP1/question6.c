#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#define NMAX 1024

#define N 5


int main(int argc, char **argv){

	int i;

	int n;

	pid_t initial_proc = getpid();

	int tube[2];

	char buf[NMAX];
	char msg[] = "bonjour";


	for(i=0; i<N; i++){

		if(pipe(tube) == -1){
			perror(NULL);
			exit(1);
		}

		switch(fork()){
			case -1: 
				perror(NULL);
				exit(1);
				
			case 0: // fils
				close(tube[1]);

				n = read(tube[0], msg, strlen(msg));


				printf("FILS: message recu %s pid %d\n", msg, getpid());


				close(tube[0]);
				break;
			default:
				close(tube[0]);

				write(tube[1], msg, strlen(msg));

				printf("PERE: message envoye %s pid %d\n", msg, getpid());

				exit(0);
		}

	}

	

}