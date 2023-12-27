%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% ID: 200007413
%%% CS3050
%%% CS@St-Andrews
%%% Exercise 2
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

operator(and).
operator(or).
operator(imp).
operator(not).

:- dynamic(truth/2).

truth(p, true).
truth(q, false).
truth(r, true).

node(0, imp, 1, 2).
node(1, p, void, void).
node(2, not, void, 3).
node(3, q, void, void).

node(10, and, 11, 12).
node(11, p, void, void).
node(12, not, void, 13).
node(13, void, void, void).

node(20, imp, 1, 2).
node(21, p, void, void).
node(22, not, void, 3).
node(23, r, void, void).


%%%% Clause 1
validFormula(ID):-
  node(ID, P, LC, RC),
  \+ P == void,
  (
    (operator(P), 
      (
        (
          \+ LC == void, 
          \+ RC == void,
          \+ ID >= LC, \+ ID >= RC,
          (
            (P == and, validFormula(LC), validFormula(RC));
            (P == or, validFormula(LC), validFormula(RC));
            (P == imp, validFormula(LC), validFormula(RC))
          ) 
        );
        (P == not, \+ ID >= RC, validFormula(RC))
      ), !
    );
    truth(P, _), LC == void, RC == void, !
  ).

%%%% Clause 2
printFormula(ID):-
  validFormula(ID),
  node(ID, P, LC, RC),
  (
    (operator(P), (
      format('~w(', [P]),
        (P == and, printFormula(LC), write(','), printFormula(RC));
        (P == or, printFormula(LC), write(','), printFormula(RC));
        (P == imp, printFormula(LC), write(','), printFormula(RC));
        (P == not, printFormula(RC))
      ), write(')'), !
    );
    write(P), !
  ).


%%%% Clause 3
calculateValues(ID):-
  validFormula(ID),
  node(ID, P, LC, RC),
  (
    (operator(P), (
        (P == and, and(LC, RC, V));
        (P == or, or(LC, RC, V));
        (P == imp, imp(LC, RC, V));
        (P == not, not(RC, V))
      ),
      asserta(truth(ID, V)), !
    );
    truth(P, V), asserta(truth(ID, V)), !
  ).

and(ID1, ID2, V):-
  calculateValues(ID1),
  calculateValues(ID2),
  truth(ID1, V1),
  truth(ID2, V2),
  (
    ((V1 == true, V2 == true), V = true);
    V = false
  ).

or(ID1, ID2, V):-
  calculateValues(ID1),
  calculateValues(ID2),
  truth(ID1, V1),
  truth(ID2, V2),
  (
    ((V1 == true; V2 == true), V = true);
    V = false
  ).

imp(ID1, ID2, V):-
  calculateValues(ID1),
  calculateValues(ID2),
  truth(ID1, V1),
  truth(ID2, V2),
  (
    ((V1 == false; V2 == true), V = true);
    V = false
  ).

not(ID, V):-
  calculateValues(ID),
  truth(ID, V1),
  (V1 == true, V = false);
    V = true.

%%%% Clause 4
isValue(ID,V):-
  calculateValues(ID),
  truth(ID, V).

 
