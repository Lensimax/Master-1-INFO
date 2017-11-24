EXEC=test 
LDFLAGS=-Wall -Werror -g -O2 -Wshadow
LDLIBS=

all: $(EXEC)

stdes.o: stdes.c stdes.h

test: test.o stdes.o

clean:
	rm $(EXEC) *.o