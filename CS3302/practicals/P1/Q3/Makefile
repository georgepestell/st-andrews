CC = clang
CFLAGS = -c -Wall -Wextra -O0
X86FLAGS = -target x86_64
ARMFLAGS = -target arm64
AFLAGS = -c -S -Wall -Wextra -fno-verbose-asm
LFLAGS = -lm -Wall -Wextra

all: exEuclidian

clean:
	rm -f exEuclidian exEuclidian.o 

exEuclidian: exEuclidian.o
	${CC} ${LFLAGS} exEuclidian.o -o exEuclidian

exEuclidian.o:
	${CC} ${CFLAGS} exEuclidian.c -o exEuclidian.o
