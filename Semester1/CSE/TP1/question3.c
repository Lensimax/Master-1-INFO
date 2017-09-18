#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>


int main(int argc, char **argv){

	int i;

	srandom(getpid());

	for(i=0; i<atoi(argv[1]); i++){

		switch(fork()){
			case 0: 
				printf("processus pid %d node %i val = %li\n", getpid(), i, random());
				break;
			case -1:
				perror(NULL);
				break;
			default:
				exit(0);
		}

	}
	exit(0);

}