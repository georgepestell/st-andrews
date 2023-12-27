import random
import progressbar
import math
import big_o

def helper(a):
  return recurse(a, 0, len(a))
def recurse(a, start, end):
  if (a[start] > a[start+1]):
    return len(a)-start-1
  x = random.randrange(start,end)
  if (a[x] < a[start]):
    return recurse(a, start, x+1)
  else:
    return recurse(a, x, end)
    

def iterate(a):
  start = 0
  end = len(a)
  x = 0
  while end - start > 1:
    x = random.randrange(start,end-1)
    if a[x] > a[x+1]:
      break
    if a[x] < a[0]:
      end = x+1
    else:
      start = x
  return len(a)-x-1

def rot(a, i):
  return a[i:] + a[:i]

def genInput(n):
  # bar.update(math.floor((pow(n,2)/pow(MAX_VALUE,2))*100))
  a = list(range(n))
  i = random.randrange(1,n)
  return a[i:] + a[:i], i

def getInput(n):
  (a, i) = genInput(n)
  return a


(a, i) = genInput(3)
print(a)
print(i)
print(iterate(a))

MAX_VALUE = 5000
REPEATS=200000


# bar = progressbar.ProgressBar(max_value=100).start()
# best, others = big_o.big_o(iterate, getInput, n_repeats=REPEATS, max_n=MAX_VALUE)


success_count = 0
for n in range(2,MAX_VALUE+2):
  (a, i) = genInput(n)
  if i != iterate(a):
    print("n", n)
    print("i", i)
    print("x", iterate(a))
  else:
    success_count+=1
print("passed:", success_count)

# bar.finish()
# print(best)
