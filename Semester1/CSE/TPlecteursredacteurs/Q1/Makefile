EXEC=test_lecteurs_redacteurs
LDFLAGS=-Wall -Werror -g -O2 -Wshadow -std=gnu11
LDLIBS=-lpthread

all: $(EXEC)

test_lecteurs_redacteurs: test_lecteurs_redacteurs.c lecteur_redacteur.c

test_valgrind:
	valgrind --tool=helgrind ./test_lecteurs_redacteurs 2 2 2

clean:
	rm $(EXEC) *.o