%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% ID: 200007413
%%% CS3050
%%% CS@St-Andrews
%%% Exercise 1
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%% Facts

% Create objective and requirements 
objective(obj).
objective(obj1).
requirement(rq).
requirement(rq1).
requirement(rq2).

objectiveDependency(obj, rq).
objectiveDependency(obj, rq1).
objectiveDependency(obj1, rq2).

component(cp1).
component(cp2).
component(cp3).
component(cp4).

requirementDependency(rq, cp1).
requirementDependency(rq, cp3).
requirementDependency(rq2, cp4).

subcomponent(cp1, cp2).

functionality(ft1, cp2, 1).
functionality(ft2, cp3, 2).
functionality(ft3, cp3, 3).
functionality(ft4, cp2, 0).
functionality(ft5, cp2, 5).
functionality(ft6, cp4, 100).

maxPriority([F], Max):-
  functionality(F, _, P),
  Max is P.
maxPriority([F|List], Max):-
  maxPriority(List, Submax),
  functionality(F, _, P),
  (P > Submax -> Max is P; Max is Submax).

dependency(A, B):-
  objectiveDependency(B, A);
  requirementDependency(B, A);
  subcomponent(B, A);
  functionality(A, B, _).

dependency(A, B) :- 
  (
    objectiveDependency(C, A);
    requirementDependency(C, A);
    subcomponent(C, A);
    functionality(A, C, _)
  ),
  dependency(C, B).

printList([Head]):-
  write(Head),nl.
printList([Head|List]):-
  write(Head),nl,
  printList(List).

%%%% Clause 1
haveSameObj(R1,R2):-
  requirement(R1),
  requirement(R2),
  objectiveDependency(O, R1),
  objectiveDependency(O, R2),
  R1\==R2.

%%%% Clause 2
belongsTo(A,B):-
  dependency(A, B).
 
%%%% Clause 3
shareDevelop(F1,F2):-
  functionality(F1, _, _),
  functionality(F2, _, _),
  dependency(F1, X),
  dependency(F2, X).

%%%% Clause 4
printAllCom(O):-
  objective(O),
  findall(C, (dependency(C, O), component(C)), Components),
  printList(Components).

%%%% Clause 5
isCompound(C):-
  component(C),
  subcomponent(C, _).

%%%% Clause 6
printAllAtomic(R):-
  requirement(R),
  findall(
    C, 
    (
      dependency(C, R),
      component(C),
      \+ isCompound(C)
    ), 
    AtomicComponents
  ),
  printList(AtomicComponents).



%%%% Clause 7
hasPriorityCom(C1,C2):-
  component(C1),
  component(C2),
  findall(F1, (dependency(F1, C1), functionality(F1, _, _)), C1Deps),
  findall(F2, (dependency(F2, C2), functionality(F2, _, _)), C2Deps),
  maxPriority(C1Deps, C1Priority),
  maxPriority(C2Deps, C2Priority),
  C1Priority > C2Priority.
       
%%%% Clause 8
hasHigherPriority(F,N):-
  functionality(F, _, P),
  P >= N.

