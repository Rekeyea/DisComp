finish = [False]

#Horror con variables globales
def t(v):
	if v == "FIN":
		finish[0] = True

lista = range(100) + ["FIN"] + range(21)
indice = 0
maxIndice = lista.size() - 1 #en python real no existe size, es len()
#maxIndice = len(lista) - 1 
while not finish[0]:
	if indice < maxIndice:
		t(lista[indice])
		indice = indice + 1

print "Fin de loop en indice: ",indice-1
