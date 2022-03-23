def lcg(X, a, c, m):
    return (a * X + c) % m

seed = 1
a = 16807
c = 0
m = 2**31
no_numbers = 1000
X = seed
for i in range(no_numbers):
    X = lcg(X, a, c, m)
    u = X / float(m)
    
    if u < 0.5:
        print(0)
    elif u < 0.7:
        print(1)
    else:
        print(2)
