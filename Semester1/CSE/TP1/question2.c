#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>


int main(int argc, char **argv){

	int n, i;

	if(argc != 2){
		printf("mauvais nombre argument\n");
		exit(1);
	} else {
		n = atoi(argv[1]);
	}

	for(i=0; i<n; i++){
		switch(fork()){
			case -1:
				perror("fork");
				exit(1);

			case 0: // fils
				printf("creation\n");
				break;
			default:
				exit(0);
		}
	}

	exit(0);

}