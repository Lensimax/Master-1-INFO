CC=gcc

# uncomment to compile in 32bits mode (require gcc-*-multilib packages
# on Debian/Ubuntu)
#HOST32= -m32

CFLAGS= $(HOST32) -Wall -Werror -std=c99 -g -DMEMORY_SIZE=128000
CFLAGS+= -DDEBUG
LDFLAGS= $(HOST32)
TESTS+=test_init test_basic  test_basic2 test_basic3 test_basic4 test_limit
PROGRAMS=memshell $(TESTS)

.PHONY: clean all test_ls

all: $(PROGRAMS)
	# for file in $(TESTS);do echo '\nTest fichier: ' $$file '\n'; ./$$file; done

# dépendences des binaires
$(PROGRAMS): %: mem.o common.o

# dépendances des fichiers objects
$(patsubst %.c,%.o,$(wildcard *.c)): %.o: .%.deps

.%.deps: %.c
	$(CC) $(CPPFLAGS) -MM $< | sed -e 's/\(.*\).o: /.\1.deps \1.o: /' > $@

-include $(wildcard .*.deps)

# seconde partie du sujet
libmalloc.so: malloc_stub.o mem.o
	$(CC) -shared -Wl,-soname,$@ $^ -o $@

test_ls: libmalloc.so
	LD_PRELOAD=./libmalloc.so ls

# nettoyage
clean:
	rm -f *.o $(PROGRAMS) libmalloc.so .*.deps
