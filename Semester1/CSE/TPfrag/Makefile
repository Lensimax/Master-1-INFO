CC=gcc

# uncomment to compile in 32bits mode (require gcc-*-multilib packages
# on Debian/Ubuntu)
#HOST32= -m32

CFLAGS+= $(HOST32) -Wall -Werror -std=gnu99 -g -DMEMORY_SIZE=128000
CFLAGS+= -DDEBUG
# pour tester avec ls
CFLAGS+= -fPIC
CFLAGS+= -lpthread
LDFLAGS= $(HOST32)
TESTS+=test_init
PROGRAMS=memshell $(TESTS)

.PHONY: clean all test_ls dist

all: $(PROGRAMS)
	for file in $(TESTS);do ./$$file; done

# dépendences des binaires
$(PROGRAMS) libmalloc.so: %: mem.o common.o

# dépendances des fichiers objects
$(patsubst %.c,%.o,$(wildcard *.c)): %.o: .%.deps

.%.deps: %.c
	$(CC) $(CFLAGS) -MM $< | sed -e 's/\(.*\).o: /.\1.deps \1.o: /' > $@

-include $(wildcard .*.deps)

# seconde partie du sujet
libmalloc.so: malloc_stub.o
	$(CC) -shared -Wl,-soname,$@ $^ -o $@

test_ls: libmalloc.so
	LD_PRELOAD=./libmalloc.so ls

# nettoyage
clean:
	rm -f *.o $(PROGRAMS) libmalloc.so .*.deps

ifeq ($(SOURCES_LIST),)
SOURCES_LIST=$(wildcard *.c)
endif

dist: README Makefile $(SOURCES_LIST) $(wildcard *.h) test_init.c
	mkdir memoire
	cp $^ memoire
	tar cvzf memoire.tar.gz memoire
	rm -r memoire
