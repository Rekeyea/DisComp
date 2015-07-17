a = []
for i in range(0,20,2):
	a.append( (i, i%2*3**2/2+9//4 + (3 >> 1) + (21 << 3) + (41 + (~ i)) - (i*2 | 4) + (i ^ 31) + (21 ^ i )) )
	print a[-1][0], " --> ",a[-1][1]

print "\nCreo dict de lista de tuplas, resultado seguro difiere con el de python ya que ordenan claves distinto\n"
d = dict(a)
for item in d.items():
	print item[0], " --> ", item[1]
	
	
TUPLA = type((1,))

for x in a + d.keys() + d.values(): 
	print x
	if type(x) == TUPLA:
		x = x[0]
		
	if x == 177:
		print "Encontre 177, haciendo break"
		break
	if int(x) % 2 == 0:
		print "Encontre un par: ", str(x), " continue"
	
	print "no hice ni break ni continue"
	
	
print "-----------"
print [2] * 10
print (1,2) * 3
	
#Pruebo agregar un valor a variables globales
def prueba(valor):
	a.append(valor)
	d['valorPrueba'] = valor
	
	
b = range(20)
print b
b[2:5] = a[1:5]
print b
b[:100] = "hola lalalala"
print b
	
print b[:]
print b[:-2]
print b[0:-1:2]

print "-----------"
b[5:4] = b
print b 

b[-1:-3] = ['menos 1','menos 2']
print b
	
print "-----------"

prueba(valor = "valor super importante de prueba")
print d['valorPrueba']
print a[-1]
