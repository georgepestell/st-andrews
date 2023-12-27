all: tests sched

clean:
	rm -f printchars
	rm -f sched
	rm -f ./tests/test_process
	rm -f ./tests/test_sched
	rm -f ./tests/test_parser_fifo
	rm -f ./tests/test_parser_rr
	rm -f ./tests/test_parser_priority
	rm -f ./tests/test_sched_fifo
	rm -f ./tests/test_sched_rr
	rm -f ./tests/test_sched_priority
	rm -f ./tests/test_performance

sched: printchars
	gcc -o sched main.c

printchars:
	gcc -o printchars printchars.c

tests: test_process test_sched test_parser test_sched_fifo test_sched_rr test_sched_priority test_performance 

test_process:
	gcc -o ./tests/test_process ./tests/test_process.c

test_sched: 
	gcc -o ./tests/test_sched ./tests/test_sched.c

test_parser: printchars
	gcc -o ./tests/test_parser_fifo ./tests/test_parser_fifo.c
	gcc -o ./tests/test_parser_rr ./tests/test_parser_rr.c
	gcc -o ./tests/test_parser_priority ./tests/test_parser_priority.c

test_sched_fifo:
	gcc -o ./tests/test_sched_fifo ./tests/test_sched_fifo.c

test_sched_rr:
	gcc -o ./tests/test_sched_rr ./tests/test_sched_rr.c

test_sched_priority:
	gcc -o ./tests/test_sched_priority ./tests/test_sched_priority.c

test_performance:
	gcc -o ./tests/test_performance ./tests/test_performance.c

