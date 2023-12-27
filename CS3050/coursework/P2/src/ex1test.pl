%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% ID:
%%% CS3050
%%% CS@St-Andrews
%%% Test script for Exercise 1
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


% ***************** PLEASE DO NOT DELETE ** STARTS ***********************

testex1:-
write('=======Start Tests for Exercise 1'),
nl,
test11,
nl,
test12,
nl,
test13,
nl,
test14,
nl,
test15,
nl,
test16,
nl,
test17,
nl,
test18,
nl,
write('======Stop Tests for Exercise 1').


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% test clause 1
test11:-
    nl,
    write('--- Test Query 1 for Clause 1 ---'),
    nl,
    test111,
    nl,
    write('--- Test Query 2 for Clause 1 ---'),
    nl,
    test112.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% test clause 2
test12:-
    nl,
    write('--- Test Query 1 for Clause 2 ---'),
    nl,
    test121,
    nl,
    write('--- Test Query 2 for Clause 2 ---'),
    nl,
    test122.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% test clause 3
test13:-
    nl,
    write('--- Test Query 1 for Clause 3 ---'),
    nl,
    test131,
    nl,
    write('--- Test Query 2 for Clause 3 ---'),
    nl,
    test132.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% test clause 4
test14:-
    nl,
    write('--- Test Query 1 for Clause 4 ---'),
    nl,
    test141,
    nl,
    write('--- Test Query 2 for Clause 4 ---'),
    nl,
    test142.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% test clause 5
test15:-
    nl,
    write('--- Test Query 1 for Clause 5 ---'),
    nl,
    test151,
    nl,
    write('--- Test Query 2 for Clause 5 ---'),
    nl,
    test152.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% test clause 6
test16:-
    nl,
    write('--- Test Query 1 for Clause 6 ---'),
    nl,
    test161,
    nl,
    write('--- Test Query 2 for Clause 6 ---'),
    nl,
    test162.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% test clause 7
test17:-
    nl,
    write('--- Test Query 1 for Clause 7 ---'),
    nl,
    test171,
    nl,
    write('--- Test Query 2 for Clause 7 ---'),
    nl,
    test172.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%% test clause 8
test18:-
    nl,
    write('--- Test Query 1 for Clause 8 ---'),
    nl,
    test181,
    nl,
    write('--- Test Query 2 for Clause 8 ---'),
    nl,
    test182.


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

test111:-
  test_yes(haveSameObj(rq, rq1)),nl,
  test_no(haveSameObj(rq, rq)).
test112:-
  test_no(haveSameObj(rq, rq2)).

test121:-
  test_yes(belongsTo(ft1, obj)),nl,
  test_yes(belongsTo(cp1, obj)),nl,
  test_yes(belongsTo(cp2, obj)),nl,
  test_yes(belongsTo(rq, obj)),nl,
  test_no(belongsTo(obj, obj)).
test122:-
  test_no(belongsTo(ft1, obj2)),nl,
  test_no(belongsTo(cp1, cp3)).

test131:-
  test_yes(shareDevelop(ft1, ft3)),nl,
  test_yes(shareDevelop(ft3, ft2)),nl,
  test_yes(shareDevelop(ft1, ft1)),nl,
  test_no(belongsTo(ft1, ft6)).
test132:-
  test_no(shareDevelop(ft1, obj)),nl,
  test_no(shareDevelop(rq, obj)),nl,
  test_no(shareDevelop(rq, ft1)).

test141:-
  test_yes(printAllCom(obj)),nl,
  test_yes(printAllCom(obj1)).
test142:-
  test_no(printAllCom(cp2)),nl,
  test_no(printAllCom(ft1)).

test151:-
  test_yes(isCompound(cp1)),nl,
  test_no(printAllCom(cp3)),nl,
  test_no(printAllCom(cp2)).
test152:-
  test_no(isCompound(obj)),nl,
  test_no(printAllCom(rq1)).

test161:-
  test_yes(printAllAtomic(rq)),nl,
  test_yes(printAllAtomic(rq2)),nl,
  test_no(printAllAtomic(rq1)).
test162:-
  test_no(printAllAtomic(obj)),nl,
  test_no(printAllAtomic(ft2)),nl,
  test_no(printAllAtomic(cp1)).

test171:-
  test_yes(hasPriorityCom(cp1, cp3)),nl,
  test_yes(hasPriorityCom(cp4, cp1)),nl,
  test_no(hasPriorityCom(cp1, cp1)),nl,
  test_no(hasPriorityCom(cp1, cp2)).
test172:-
  test_no(hasPriorityCom(cp4, rq)),nl,
  test_no(hasPriorityCom(obj, rq1)).

test181:-
  test_yes(hasHigherPriority(ft4, 0)),nl,
  test_yes(hasHigherPriority(ft6, 99)),nl,
  test_yes(hasHigherPriority(ft3, 1)),nl,
  test_no(hasHigherPriority(ft3, 5)),nl,
  test_no(hasHigherPriority(ft4, 5)).
test182:-
  test_no(hasHigherPriority(obj, 1)),nl,
  test_no(hasHigherPriority(rq1, 0)),nl,
  test_no(hasHigherPriority(cp2, 2)).






 
