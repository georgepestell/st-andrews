import random
import big_o
import progressbar
import math

def rot(a, i):
  return a[i:] + a[:i]

def q4_1_a(a):
  n = len(a)
  for i in range(n):
    b = rot(list(range(n)), i)
    ok = True
    for j in range(n):
      if a[j] != b[j]:
        ok = False
        break
    if ok:
      return i
  return False

def genInput(n):
  bar.update(n)
  a = list(range(n))
  i = random.randrange(0,n)
  return rot(a,i)



MAXN=1000
MEASURES=50
REPEATS=100

bar = progressbar.ProgressBar(max_value=MAXN).start()

best, others = big_o.big_o(q4_1_a, genInput, n_repeats=REPEATS, max_n=MAXN, n_measures=MEASURES)
bar.finish()
print(best)

