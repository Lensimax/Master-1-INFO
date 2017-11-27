EXEC=main
LDFLAGS=-Wall -Werror -g -O2 -Wshadow -std=gnu11
LDLIBS=-lpthread

all: $(EXEC)

main: main.o restaurant.o 
restaurant.o: restaurant.c restaurant.h
main.o: main.c restaurant.o