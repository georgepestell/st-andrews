%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% ID:
%%% CS3050
%%% CS@St-Andrews
%%% Test script for Exercise 3
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


% ***************** PLEASE DO NOT DELETE ** STARTS ***********************

testex3:-
write('=======Start Tests for Exercise 3'),
nl,
test31,
nl,
test32,
nl,
test33,
nl,
write('======Stop Tests for Exercise 3').


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% test clause 1
test31:-
    nl,
    write('--- Test Query 1 for Clause 1 ---'),
    nl,
    test311,
    nl,
    write('--- Test Query 2 for Clause 1 ---'),
    nl,
    test312.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% test clause 2
test32:-
    nl,
    write('--- Test Query 1 for Clause 2 ---'),
    nl,
    test321,
    nl,
    write('--- Test Query 2 for Clause 2 ---'),
    nl,
    test322.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% test clause 3
test33:-
    nl,
    write('--- Test Query 1 for Clause 3 ---'),
    nl,
    %test331,
    nl,
    write('--- Test Query 2 for Clause 3 ---'),
    nl.
    %%test332.

 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% Clauses to decide the expected answer


% If a clause is expected to return no, use predicate test_no<query,queryNumber>.
% else use predicate ans_yes<query,queryNumber>.

test_yes(S):-S, write('Pass (Yes)'),!.
test_yes(S):- write('**** Fail ').


test_no(S):- \+ S, write('Pass (No)'), !.
test_no(S):- write('**** Fail ').


% ************** PLEASE DO NOT DELETE ** ENDS ******************


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%% Complete the test cases below  by uncommenting, including  your query and place it within the appropriate test predicate (yes/no) depending on your expected output.

 
test311:-
  test_yes(valid(a)),nl,
  test_yes(valid(b)).
test312:-
  test_no(valid(f)),nl,
  test_no(valid(g)).

test321:-
  test_yes(isOnPossible(a, d, 3)),nl,
  test_no(isOnPossible(a, d, 2)),nl,
  test_yes(isOnPossible(a, c, 10)),nl,
  test_no(isOnPossible(a, c, 4)).
test322:-
  test_no(isOnPossible(a, a, 1)),nl,
  test_no(isOnPossible(b, b, 10)).

%test331:-
%  test_yes(printAllPossible(3)),nl,
%  test_yes(printAllPossible(4)),nl.
%test332:-
%  test_no(printAllPossible(0)).

 





 
