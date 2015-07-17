l = [22, True, "una lista", [1, 2]]
mi_var = l[0] # mi_var vale 22
print mi_var

l = [99, True, "una lista", [1, 2]]
mi_var = l[0:2] # mi_var vale [99, True]
print mi_var
mi_var = l[0:4:2] # mi_var vale [99, "una lista"]
print mi_var

l = [99, True, "una lista"]
mi_var = l[1:] # mi_var vale [True, "una lista"]
print mi_var
mi_var = l[:2] # mi_var vale [99, True]
print mi_var
mi_var = l[:] # mi_var vale [99, True, "una lista"]
print mi_var
mi_var = l[::2] # mi_var vale [99, "una lista"]
print mi_var

l = [99, True, "una lista", [1, 2]]
l[0:2] = [0, 1] # l vale [0, 1, "una lista", [1, 2]]
print l
