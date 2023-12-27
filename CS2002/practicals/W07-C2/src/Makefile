RunCounter : wordCounter.o wordList.o word.o
	clang wordCounter.o wordList.o word.o -o RunCounter -Wall -Wextra -g

word.o : 
	clang word.c -c -o word.o -Wall -Wextra -g

wordList.o :
	clang wordList.c -c -o wordList.o -Wall -Wextra -g

wordCounter.o :
	clang wordCounter.c -c -o wordCounter.o -Wall -Wextra -g

Tests :
	make TestMemoryOverflow
	rm -f *.o
	make TestWordList
	make TestWord

TestWord : tests/testWord.o word.o
	clang tests/testWord.o word.o -o TestWord -Wall -Wextra -g
	sed -i 's/.*#define TESTING_COUNT_MAX/\/\/#define TESTING_COUNT_MAX/g' word.h wordList.h

TestWordList : tests/testWordList.o wordList.o word.o
	clang tests/testWordList.o wordList.o word.o -o TestWordList -Wall -Wextra -g
	sed -i 's/.*#define TESTING_COUNT_MAX/\/\/#define TESTING_COUNT_MAX/g' word.h wordList.h

TestMemoryOverflow : tests/testMemoryOverflow.o word.o wordList.o
	clang tests/testMemoryOverflow.o word.o wordList.o -o TestMemoryOverflow -Wall -Wextra -g
	sed -i 's/.*#define TESTING_MEMORY_LIMIT/\/\/#define TESTING_MEMORY_LIMIT/g' word.h wordList.h

tests/testWord.o :
	sed -i 's/.*\/\/#define TESTING_COUNT_MAX/#define TESTING_COUNT_MAX/g' word.h wordList.h
	clang tests/testWord.c -c -o tests/testWord.o -Wall -Wextra -g

tests/testWordList.o :
	sed -i 's/.*\/\/#define TESTING_COUNT_MAX/#define TESTING_COUNT_MAX/g' word.h wordList.h
	clang tests/testWordList.c -c -o tests/testWordList.o -Wall -Wextra -g

tests/testMemoryOverflow.o :
	sed -i 's/.*\/\/#define TESTING_MEMORY_LIMIT/#define TESTING_MEMORY_LIMIT/g' word.h wordList.h
	clang tests/testMemoryOverflow.c -c -o tests/testMemoryOverflow.o -Wall -Wextra -g

clean :
	rm -f *.o RunCounter TestWord TestWordList TestMemoryOverflow tests/*.o
