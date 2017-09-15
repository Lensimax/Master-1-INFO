#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#define NMAX 1024


int main(int argc, char **argv){

	int n;
	int tube[2];
	int fd;

	char buf[NMAX];

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

			while((n = read(tube[0], buf, NMAX)) > 0){
				write(1, buf, n);
			}


			close(tube[0]);

			break;

		default: // père

			close(tube[0]);

			fd = open(argv[1], O_RDONLY);

			while((n = read(fd, buf, NMAX)) > 0){
				write(tube[1], buf, n);
			}

			close(tube[1]);
			break;
	}

	exit(0);


}