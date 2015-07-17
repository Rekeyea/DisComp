objeto = object()
d = {
	'k1':1,
	'k2':2,
	'k3': [1,2,3,4],
	'k4': (1,2,3,'a'),
	'k5': {'hola':[1,2].pop(0)},
	objeto: 'objeto',
	3L : 'long',
	2.2 : 'float',
	True : 'bool true',
	False : 'bool false'
}

print d['k1']
print d['k2']
print d.has_key('k3')
print d[objeto]
print d[3L]
print d[2.2]
print d[True]
print d[False]
print "__hash__() de objeto: " + str(d[objeto].__hash__()), " ", "Es distinto en cada ejecucion."
print d['k5']['hola']
print d['k4'][1]
print d['k3'][d['k4'][1]]
print d.pop('k3')
print d.pop('k4')
print d.pop('k5')
print d.items()
print d.keys()
print d.values()
print dict.values(d) #llamo desde el metodo de clase en vez de objeto.

print "A partir de esta linea, debe haber un error de clave, por acceder a clave no existente del diccionario"
print d['k5']