EXEC=test test2
LDFLAGS=-Wall -Werror -Wshadow -g
LDLIBS=

all: $(EXEC)

stdes.o: stdes.h

%.o: %.c
	$(CC) $(LDFLAGS) -c $<

test: test.o stdes.o
	$(CC) -o $@ $^

test2: test2.o stdes.o
	$(CC) -o $@ $^

clean:
	rm $(EXEC) *.o