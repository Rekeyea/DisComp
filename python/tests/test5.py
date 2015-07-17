print "----- Test listas-------"

a = range(10)
b = range(1,10,2)

print a
print b

c = list(a)
c.extend(b)
print c

c.insert(0,"Hola en 0")
print c
c.insert(-1, "chau en -1")
print c

print "Resultado de pop: ", c.pop(-1)
print c

c.insert(1,"insert en 1")
print c

c.insert(12312323,"insert fuera de rango")
print c

c.insert(-100213, "insert fuera de rango negativo")
print c

c.append("append, debo estar al final")
print "Luego de append, ver elemento del final: ", c
print "Ultimo elemento con indice negativo: ", c[-1]

print "Indice de 'insert fuera de rango negativo': ", c.index("insert fuera de rango negativo")
print "Indice de 'chau en -1': ", c.index("chau en -1")
print "Indice de 1: ", c.index(1)

print "Intento hacer .index de elemento que no existe, esto deberia explotar."
print "Indice de elemento que no existe: ", c.index("hola no existo")
