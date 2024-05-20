def translate(x, y, z):
    return np.array([
    [1, 0, 0, x],
    [0, 1, 0, y],
    [0, 0, 1, z],
    [0, 0, 0, 1],
    ])

def scale(x, y, z):
    return np.array([
    [x, 0, 0, 0],
    [0, y, 0, 0],
    [0, 0, z, 0],
    [0,0,0,1]
])

def rotateX(angle):
    return  np.array([
    [1, 0,               0,              0],
    [0, cos(angle),   -sin(angle), 0],
    [0, sin(angle),   cos(angle),  0],
    [0, 0,               0,              1],
    ])

def rotateY(angle):
    return np.array([
    [cos(angle),  0, sin(angle), 0],
    [0,              1, 0,             0],
    [-sin(angle), 0, cos(angle), 0],
    [0,              0, 0,             1],
    ])

def rotateZ(angle):
    return np.array([
    [cos(angle), -sin(angle), 0, 0],
    [sin(angle), cos(angle), 0, 0],
    [0,0,1,0],
    [0,0,0,1]
])

import numpy as np
from math import  sin, cos

delta = np.array([0, 2, 2])
sigma = np.array([1.2, 1.2, 1,2])
theta = np.array([0.2, 0.2, 0.1])

####################
# Q1 SETUP
####################

x = 0
y = 1 
z = 2

T = translate(delta[x], delta[y], delta[z])

S = scale(sigma[x], sigma[y], sigma[z])

RX= rotateX(theta[x])
RY= rotateY(theta[y])
RZ = rotateZ(theta[z])

M_model = np.matmul(RZ, (np.matmul(RY, np.matmul(RX, np.matmul(S, T)))));

V = np.array([ 
    [-2,2,2,-2,-2,2,2,-2,-2,2],
    [-1, -1, 1, 1, -1, -1, 1,1, -0.5, -0.5],
    [3,3,3,3,-3,-3,-3,-3,-1,-1],
    [1,1,1,1, 1, 1, 1, 1, 1, 1]
    ])

V_prime = np.matmul(M_model,V)

VPT = np.transpose(V_prime)

print("--------------------")
print("Q1.c:")
print("--------------------")

print(M_model.round(5))

print("--------------------")
print("Q1.d:")
print("--------------------")

print(V_prime.round(5))

print("--------------------")
print("Q2.a:")
print("--------------------")

P1P = VPT[0]
P2P = VPT[1]
P3P = VPT[2]
P4P = VPT[3]
P5P = VPT[4]

print(P1P.round(5))
print(P2P.round(5))
print(P3P.round(5))
print(P4P.round(5))
print(P5P.round(5))

print("--------------------")
print("Q2.b:")
print("--------------------")


v1 = P1P - P2P
v2 = P1P - P4P
v3 = P1P - P5P

print(v1.round(5))
print(v2.round(5))
print(v3.round(5))

print("--------------------")
print("Q2.c:")
print("--------------------")

print(np.dot(v1, v2).round(5))
print(np.dot(v1, v3).round(5))
print(np.dot(v2, v3).round(5))

print("--------------------")
print("Q2.d:")
print("--------------------")

v1_prime = P1P[0:3] - P2P[0:3]
v2_prime = P1P[0:3] - P3P[0:3]
v3_prime = P1P[0:3] - P4P[0:3]

print("v1': ")
print(v1_prime)
print("v2': ")
print(v2_prime)
print("v3': ")
print(v3_prime)

n = np.cross(v1_prime, v2_prime)

print("normal: ")
print(n)

print(np.dot(n, v3_prime))

print("--------------------")
print("Q3.b:")
print("--------------------")

d_c = 3
theta_c = 0.25
phi_c = 0.2

Q2_T = translate(0, 0, d_c)
Q2_RX = rotateX(theta_c)
Q2_RY = rotateY(phi_c)

M_view = np.matmul(Q2_RY, np.matmul(Q2_RX, Q2_T))

print(M_view.round(5))

print("--------------------")
print("Q3.c:")
print("--------------------")

M_model_view = np.matmul(M_view, M_model)

print(M_view.round(5))

print("--------------------")
print("Q3.d:")
print("--------------------")

V_model_view = np.matmul(M_model_view,V)
print(V_model_view.round(5))

# Calculate coordinates to check distance doubles in next question

V_MVT = np.transpose(V_model_view)

Q3_P1 = V_MVT[0]
Q3_P2 = V_MVT[1]
Q3_P4 = V_MVT[3]

Q3_v1 = Q3_P1 - Q3_P2
Q3_v2 = Q3_P1 - Q3_P4

print("--------------------")
print("Q4:")
print("--------------------")

# Create the scale matrix
Q4_S = scale(2,1,1)

print("Initial Rotation Matrix")
print(Q4_S)


print("M_model_view Inverse")
print(np.linalg.inv(M_model_view).round(5))

# Apply the model view to get the in-place scale matrix
Q4_S_MV = np.matmul(M_model_view, np.matmul(Q4_S, np.linalg.inv(M_model_view)))

print("In-Place Scale Matrix:")
print(Q4_S_MV.round(5))

# Check that the scale matrix doubles the distance from P1 to P2

Q4_V = np.matmul(Q4_S_MV, V)

Q4_VT = np.transpose(Q4_V)

Q4_P1 = Q4_VT[0]
Q4_P2 = Q4_VT[1]
Q4_P4 = Q4_VT[3]

Q4_v1 = Q4_P1 - Q4_P2
Q4_v2 = Q4_P1 - Q4_P4

print("Checking v1 doubles whilst v2 remains")
print("v1")
print("Before: " + np.array2string(Q3_v1))
print("Scaled: " + np.array2string(Q4_v1))
print("v2")
print("Before: " + np.array2string(Q3_v2))
print("Scaled: " + np.array2string(Q4_v2))
print("V vs Scaled V")
print(V_model_view)
print(Q4_V)


