#include "stdes.h"
#include <stdlib.h>
#include <stdio.h>

int main(int argc, char *argv[])
{
	FICHIER *f1;
	FICHIER *f2;
	// char c;

	if (argc != 3)
		exit(-1);

	f1 = ouvrir (argv[1], 'L');
	if (f1 == NULL)
		exit (-1);

	f2 = ouvrir (argv[2], 'E');
	if (f2 == NULL)
		exit (-1);

	// while (lire (&c, 1, 1, f1) == 1) {
	// 	printf("%c",c);
	// 	ecrire (&c, 1, 1, f2);
	// }
	char q = 'q';
	int i = 0;
	char s[10] = "anus";
	fliref(f1, "%do%co%s", &i, &q, &s);
	fecriref(f2, "int %d string %s char %c\n", i, s, q);

	fermer (f1);
	fermer (f2);

	return 0;
}
