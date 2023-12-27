%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% ID: 200007413
%%% CS3050
%%% CS@St-Andrews
%%% Exercise 3
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

a.
b.
c.
d.
f.
g.
table.
none.

:- dynamic(on/3).

on(none, a, 1).
on(a, b, 1).
on(b, c, 1).
on(c, table, 1).
on(d, table, 1).
on(none, d, 1).

on(g, none, 1).
on(f, g, 1).

isClear(A, X):-
  on(none, A, X).
isOnTable(A, X):-
  on(A, table, X).

canBeClear(A, X):-
  X > 0,
  (
    (
      SX is X - 1,
      canPutBack(A, SX), !
    );
    (
      SX is X - 1,
      on(B, A, SX),
      canPutBack(B, SX), !
    )
  ).
canBeOnTable(A, X):-
  X > 0,
  (
    (
      SX is X - 1,
      isClear(A, SX),
      !
    );
    (
      SX is X - 1,
      canBeClear(A, SX)
    )
  ).

canPutOn(A, B, X):-
  X > 0,
  \+ A == B,
  (
    (
      isClear(B, X),
      isClear(A, X),
      isOnTable(A, X),
      !
    );
    (
      SX is X - 1;
      canBeClear(B, XS),
      canBeClear(A, XS),
      canBeOnTable(A, XS)
    )
  ).

canPutBack(A, X):-
  X > 0,
  (
    (
      isClear(A, X),
      !
    );
    (
      SX is X - 1;
      canBeClear(A, XS)
    )
  ).


%%%% Clause 1
valid(A):-
  on(A, Below, 1),
  on(_, A, 1),
  \+ Below == none.

%%%% Clause 2
isOnPossible(A,B,SX):-
  canPutOn(A,B,SX).

%%%% Clause 3
printAllPossible(SX):-
  findall([A,B], isOnPossible(A, B, SX), P),
  write(P).

printList([Head]):-
  write(Head),nl.
printList([Head|List]):-
  write(Head),nl,
  printList(List).
