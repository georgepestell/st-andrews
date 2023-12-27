import sys;
p = 157;
q = 163;
n = 25591;
i = 7;
j = 10831;
q_bar = -26

# p = 7;
# q = 11;
# n = 77;
# i = 13;
# j = 37;

j_p = j % (p - 1)
j_q = j % (q - 1)

def encode(m):
  return pow(m, i, n);

def decode_unoptimized(y):
  return pow(y, j, n);

def decode(y):
  m_p = pow(y, j_p, p);
  m_q = pow(y, j_q, q);

  print("m_p = " + str(m_p))
  print("m_q = " + str(m_q))

  h = (q_bar * (m_p - m_q)) % p;

  print("h = " + str(h))

  return m_q + (h * q);

if (len(sys.argv) != 2):
    exit(1)

print("j_p: " + str(j_p))
print("j_q: " + str(j_q))

y = int(sys.argv[1]);

print("encoded: " + str(y))
print("decoded_unopt: " + str(decode_unoptimized(y)))
print("decoded: " + str(decode(y)))


