CC = clang
CFLAGS = -c -Wall -Wextra -O0
X86FLAGS = -target x86_64
ARMFLAGS = -target arm64
AFLAGS = -c -S -Wall -Wextra -fno-verbose-asm
LFLAGS = -Wall -Wextra
DEPS = collatz.h

all: main collatz0.s collatz2.s collatz0-x86.s collatz2-x86.s collatz1-arm.s
clean:
	rm -f *.o main collatz0.s collatz1.s collatz2.s collatz3.s collatz0-x86.s collatz1-x86.s collatz2-x86.s collatz3-x86.s collatz0-arm.s collatz1-arm.s collatz2-arm.s collatz3-arm.s

main: main.o collatz.o $(DEPS)
	${CC} ${LFLAGS} main.o collatz.o -o main

main.o: main.c $(DEPS)
	${CC} ${CFLAGS} main.c -o main.o

collatz.o: collatz.c $(DEPS)
	${CC} ${CFLAGS} collatz.c -o collatz.o

collatz0.s: collatz.c $(DEPS)
	${CC} ${AFLAGS}   -O0 collatz.c -o collatz0.s

collatz1.s: collatz.c $(DEPS)
	${CC} ${AFLAGS}   -O1 collatz.c -o collatz1.s

collatz2.s: collatz.c $(DEPS)
	${CC} ${AFLAGS}   -O2 collatz.c -o collatz2.s

collatz3.s: collatz.c $(DEPS)
	${CC} ${AFLAGS}   -O3 collatz.c -o collatz3.s

collatz0-x86.s: collatz.c $(DEPS)
	${CC} ${AFLAGS} ${X86FLAGS}  -O0 collatz.c -o collatz0-x86.s

collatz1-x86.s: collatz.c $(DEPS)
	${CC} ${AFLAGS} ${X86FLAGS}  -O1 collatz.c -o collatz1-x86.s

collatz2-x86.s: collatz.c $(DEPS)
	${CC} ${AFLAGS} ${X86FLAGS}  -O2 collatz.c -o collatz2-x86.s

collatz3-x86.s: collatz.c $(DEPS)
	${CC} ${AFLAGS} ${X86FLAGS}  -O3 collatz.c -o collatz3-x86.s

collatz0-arm.s: collatz.c $(DEPS)
	${CC} ${AFLAGS} -O0 ${ARMFLAGS} collatz.c -o collatz0-arm.s

collatz1-arm.s: collatz.c $(DEPS)
	${CC} ${AFLAGS} -O1 ${ARMFLAGS} collatz.c -o collatz1-arm.s

collatz2-arm.s: collatz.c $(DEPS)
	${CC} ${AFLAGS} -O2 ${ARMFLAGS} collatz.c -o collatz2-arm.s

collatz3-arm.s: collatz.c $(DEPS)
	${CC} ${AFLAGS} -O3 ${ARMFLAGS} collatz.c -o collatz3-arm.s

