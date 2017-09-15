#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>

#define NMAX 1024


int main(int argc, char **argv){

	int n;
	int tube_ping[2], tube_pong[2];

	char buf[NMAX];
	char msg[] = "bonjour";

	if(pipe(tube_ping) == -1 || pipe(tube_pong) == -1){
		perror(NULL);
		exit(1);
	}

	switch(fork()){

		case -1:
			perror(NULL);
			exit(1);

		case 0: // fils

			close(tube_pong[0]);
			close(tube_ping[1]);

			n = read(tube_ping[0], buf, NMAX);
			write(tube_pong[1], buf, n);

			close(tube_pong[1]);
			close(tube_ping[0]);

			break;

		default: // père

			close(tube_pong[1]);
			close(tube_ping[0]);

			write(tube_ping[1], msg, strlen(msg));

			n = read(tube_pong[0], buf, NMAX);

			printf("msg = %s\n", buf);

			close(tube_pong[0]);
			close(tube_ping[1]);
			break;
	}

	exit(0);


}