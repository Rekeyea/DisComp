def probarString(valor):
	print "Total de a's: ", valor.count("a")
	print "find de hola: ", valor.find('hola')
	print "find de hola desde 5: ", valor.find('hola', 5)
	print "join con ', '.join(valor): ", ', '.join(valor)
	print "split con ' ': ", valor.split(" ")
	print "replace con 'reemplazame' por 'reemplazado': ", valor.replace("reemplazame", "reemplazado")
	
	
for i in range(10):
	print "Iteracion ", i
	probarString("hola" * i + "reemplazame" * (i % 2))
	print "\n--------------\n"
	
	