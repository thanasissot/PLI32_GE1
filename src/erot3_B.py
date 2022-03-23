def lcg(X, a, m):
    return (a * X) % m

# a = 16807
a = 85
# m = 2**31
m = 1024
seed = 1
no_numbers = 20

X = seed
for i in range(no_numbers):
    X = lcg(X, a, m)
    
    u = X / float(m)
    u = abs(((2*u) - 1)** (1./3.))
    print(u)
