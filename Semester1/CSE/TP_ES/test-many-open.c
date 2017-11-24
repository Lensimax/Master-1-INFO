#include "stdes.h"
#include <stdlib.h>
#include <sys/time.h>
#include <assert.h>

#define NB_FILES 15
FICHIER* files[NB_FILES];

char filename[] = "fichier_.txt";
int   file_char = 7;  /* index of the _ */

#define FILECHAR(x) (char)(0x61 + x)

#define show_files \
  do { \
    int _x; \
    ecriref ("+"); \
    for (_x = 0; _x < nb_files; _x++) \
      ecriref ("%c", FILECHAR(_x)); \
    ecriref ("\n"); \
  } while (0)



int main(int argc, char *argv[])
{
  int nb_files = 0;
  int i;
  struct timeval tv;
  gettimeofday(&tv, NULL);
  srand(tv.tv_usec);

  for (i=0; i<500;) {
    if (nb_files == NB_FILES || (nb_files > 0 && rand () & 1)) /* close files */
    {
      int n = rand () % nb_files + 1;
      i += n;
      nb_files -= n;
      while (n>0)
        fermer (files[nb_files + (--n)]);
    }
    else
    {
      int n = rand () % (NB_FILES - nb_files) + 1;
      i += n;
      nb_files += n;
      while (n>0) {
        filename[file_char] = FILECHAR(nb_files - n);
        files[nb_files - (n--)] = ouvrir (filename, 'E');
      }
    }
    show_files ;
  }
  return 0;
}
